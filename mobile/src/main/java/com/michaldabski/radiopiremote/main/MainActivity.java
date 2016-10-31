package com.michaldabski.radiopiremote.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.michaldabski.radiopiremote.BaseActivity;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.models.Status;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.StatusRequest;
import com.michaldabski.radiopiremote.control.PlaybackControlFragment;

/**
 * Created by Michal on 30/10/2016.
 */

public class MainActivity extends BaseActivity implements GsonResponseListener<Status> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Override KEY DOWN for volume keys
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            String volumeDelta = null;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    volumeDelta = PlaybackControlFragment.VOLUME_INCREMENT_VALUE;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (volumeDelta == null) {
                        volumeDelta = PlaybackControlFragment.VOLUME_DECREMENT_VALUE;
                    }
                    Status status = new Status();
                    status.setVolume(volumeDelta);
                    final StatusRequest request = StatusRequest.post(getPiRemoteApplication().getApiUrlBuilder(), status, this, this);
                    sendRequest(request);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onResponse(Status object) {
        final PlaybackControlFragment fragment = getPlaybackControlsFragment();
        if (fragment != null) {
            fragment.updateStatus(object);
        }
    }

    public PlaybackControlFragment getPlaybackControlsFragment() {
        return (PlaybackControlFragment) getSupportFragmentManager().findFragmentByTag("playback-controls");
    }
}
