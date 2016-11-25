package com.michaldabski.radiopiremote.radios;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.michaldabski.radiopiremote.BaseApiFragment;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.RadioListResponse;
import com.michaldabski.radiopiremote.api.models.RadioStation;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.PlayRequest;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    public static RadioListFragment newInstance() {
        return new RadioListFragment();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Object item = parent.getItemAtPosition(position);
        if (item instanceof RadioStation) {
            RadioStation radioStation = ((RadioStation) item);
            final ApiUrlBuilder urlBuilder = getPiRemoteApplication().getApiUrlBuilder();
            PlayRequest request = PlayRequest.playRadios(urlBuilder, Collections.singletonList(radioStation), this, null);
            sendRequest(request);
        }
    }

    @NonNull
    @Override
    protected Request<RadioListResponse> createRequest() {
        return new RadioListRequest(getApiUrlBuilder(), this, this);
    }

    @Override
    public void onResponse(RadioListResponse response) {
        super.onResponse(response);
        if (response.getPrevious() == null) {
            getAdapter().clear();
        }
        final RadioStation[] items = response.getResults();
        getAdapter().addAll(Arrays.asList(items));
    }
}
