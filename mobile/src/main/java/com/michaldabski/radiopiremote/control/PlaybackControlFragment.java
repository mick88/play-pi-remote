package com.michaldabski.radiopiremote.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.android.volley.Request;
import com.michaldabski.radiopiremote.BaseFragment;
import com.michaldabski.radiopiremote.BuildConfig;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.Status;
import com.michaldabski.radiopiremote.api.requests.ApiRequest;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.JumpRequest;
import com.michaldabski.radiopiremote.api.requests.StatusRequest;

/**
 * Created by Michal on 30/10/2016.
 */

public class PlaybackControlFragment extends BaseFragment implements View.OnClickListener, GsonResponseListener, SeekBar.OnSeekBarChangeListener {
    public static final String VOLUME_INCREMENT_VALUE = "+" + BuildConfig.VOLUME_CHANGE_STEP;
    public static final String VOLUME_DECREMENT_VALUE = "-" + BuildConfig.VOLUME_CHANGE_STEP;
    private ImageButton
        btnPlayPause,
        btnNext,
        btnPrev,
        btnStop,
        btnVolumeUp,
        btnVolumeDown;
    /**
     * Last known status
     */
    @Nullable
    Status lastStatus = null;
    private SeekBar seekVolume;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_controls, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.btnPlayPause = ((ImageButton) view.findViewById(R.id.btnPlayPause));
        this.btnNext = ((ImageButton) view.findViewById(R.id.btnNext));
        this.btnPrev = ((ImageButton) view.findViewById(R.id.btnPrev));
        this.btnStop = ((ImageButton) view.findViewById(R.id.btnStop));
        this.btnVolumeUp = ((ImageButton) view.findViewById(R.id.btnVolumeUp));
        this.btnVolumeDown = ((ImageButton) view.findViewById(R.id.btnVolumeDown));
        seekVolume = ((SeekBar) view.findViewById(R.id.seekVolume));

        btnPlayPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnVolumeUp.setOnClickListener(this);
        btnVolumeDown.setOnClickListener(this);
        seekVolume.setOnSeekBarChangeListener(this);

        fetchStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        btnPlayPause = null;
        btnNext = null;
        btnPrev = null;
        btnStop = null;
        btnVolumeUp = null;
        btnVolumeDown = null;
    }

    public void updateStatus(Status status) {
        lastStatus = status;

        switch (status.getState()) {
            case Status.STATE_PLAY:
                btnPlayPause.setImageResource(R.drawable.ic_pause);
                btnStop.setEnabled(true);
                break;

            case Status.STATE_PAUSE:
                btnPlayPause.setImageResource(R.drawable.ic_play);
                btnStop.setEnabled(true);
                break;

            case Status.STATE_STOP:
                btnPlayPause.setImageResource(R.drawable.ic_play);
                btnStop.setEnabled(false);
                break;
        }

        Boolean repeat = status.getRepeat();
        if (repeat == null) repeat = false;
        btnNext.setEnabled(repeat && !status.isLastItem());
        btnPrev.setEnabled(repeat && !status.isFirstItem());
        seekVolume.setProgress(status.getVolumeInt());
    }

    void fetchStatus() {
        final ApiUrlBuilder urlBuilder = getPiRemoteApplication().getApiUrlBuilder();
        //noinspection unchecked
        ApiRequest<Status> request = StatusRequest.get(urlBuilder, this, this);
        sendRequest(request);
    }

    @Override
    public void onClick(View view) {
        Status status = null;
        final ApiUrlBuilder urlBuilder = getPiRemoteApplication().getApiUrlBuilder();

        switch (view.getId()) {
            case R.id.btnPlayPause:
                status = new Status();
                if (lastStatus != null && Status.STATE_PLAY.equals(lastStatus.getState())) status.setState(Status.STATE_PAUSE);
                else status.setState(Status.STATE_PLAY);
                break;
            case R.id.btnNext:
                final JumpRequest nextRequest = JumpRequest.next(urlBuilder, this, null);
                sendRequest(nextRequest);
                break;
            case R.id.btnPrev:
                final JumpRequest prevRequest = JumpRequest.previous(urlBuilder, this, null);
                sendRequest(prevRequest);
                break;
            case R.id.btnStop:
                status = new Status();
                status.setState(Status.STATE_STOP);
                break;
            case R.id.btnVolumeUp:
                status = new Status();
                status.setVolume(VOLUME_INCREMENT_VALUE);
                break;
            case R.id.btnVolumeDown:
                status = new Status();
                status.setVolume(VOLUME_DECREMENT_VALUE);
                break;
        }

        // use GET method if not sending status update
        final int method = status == null ? Request.Method.GET : Request.Method.POST;
        final ApiRequest<Status> request = new ApiRequest<>(method, urlBuilder, ApiUrlBuilder.ENDPOINT_STATUS, Status.class, this, this);
        request.setObject(status);
        sendRequest(request);
    }

    @Override
    public void onResponse(Object object) {
        if (object instanceof Status) {
            updateStatus((Status) object);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Status status = new Status();
        status.setVolumeInt(seekBar.getProgress());

        final ApiUrlBuilder urlBuilder = getPiRemoteApplication().getApiUrlBuilder();
        final ApiRequest<Status> request = new ApiRequest<>(Request.Method.POST, urlBuilder, ApiUrlBuilder.ENDPOINT_STATUS, Status.class, this, this);
        request.setObject(status);
        sendRequest(request);
    }
}
