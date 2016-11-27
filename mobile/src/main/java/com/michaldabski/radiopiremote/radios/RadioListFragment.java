package com.michaldabski.radiopiremote.radios;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.QueueItem;
import com.michaldabski.radiopiremote.api.models.QueueResponse;
import com.michaldabski.radiopiremote.api.models.RadioListResponse;
import com.michaldabski.radiopiremote.api.models.RadioStation;
import com.michaldabski.radiopiremote.api.requests.EnqueueRequest;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.PlayRequest;
import com.michaldabski.radiopiremote.base.BaseApiFragment;
import com.michaldabski.radiopiremote.queue.MpdItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Michal on 21/11/2016.
 */

public class RadioListFragment extends BaseApiFragment<RadioListResponse, BaseMpdModel> implements AdapterView.OnItemClickListener, GsonResponseListener<RadioListResponse> {

    @NonNull
    @Override
    protected ArrayAdapter<BaseMpdModel> createAdapter() {
        final ImageLoader imageLoader = getPiRemoteApplication().getImageLoader();
        return new MpdItemAdapter(getContext(), new ArrayList<BaseMpdModel>(), imageLoader);
    }

    public static RadioListFragment newInstance() {
        return new RadioListFragment();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Object item = parent.getItemAtPosition(position);
        if (item instanceof RadioStation) {
            RadioStation radioStation = ((RadioStation) item);
            PlayRequest request = PlayRequest.playRadios(getUrlBuilder(), Collections.singletonList(radioStation), this, null);
            sendRequest(request);
        }
    }

    @NonNull
    @Override
    protected Request<RadioListResponse> createRequest(int page) {
        final ApiUrlBuilder urlBuilder = getUrlBuilder();
        return new RadioListRequest(urlBuilder.getRadiosUrl(page), this, this);
    }

    @Override
    public void onResponse(RadioListResponse response) {
        super.onResponse(response);
        final RadioStation[] items = response.getResults();
        getAdapter().addAll(Arrays.asList(items));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Object item = adapterView.getItemAtPosition(position);
        if (item instanceof RadioStation) {
            QueueItem queueItem = new QueueItem(((RadioStation) item));
            EnqueueRequest request = new EnqueueRequest(getUrlBuilder(), queueItem, this, new GsonResponseListener<QueueResponse>() {
                @Override
                public void onResponse(QueueResponse responseObject) {
                    Toast.makeText(getContext(), getString(R.string.enqueued_s, item), Toast.LENGTH_SHORT).show();
                }
            });
            sendRequest(request);
            return true;
        }
        return false;
    }
}
