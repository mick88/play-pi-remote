package com.michaldabski.radiopiremote.radios;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.toolbox.ImageLoader;
import com.michaldabski.radiopiremote.BaseFragment;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.QueueItem;
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

public class RadioListFragment extends BaseFragment implements AdapterView.OnItemClickListener, GsonResponseListener<RadioListResponse> {
    private MpdItemAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ImageLoader imageLoader = getPiRemoteApplication().getImageLoader();
        this.adapter = new MpdItemAdapter(getContext(), new ArrayList<BaseMpdModel>(), imageLoader);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
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
            PlayRequest request = PlayRequest.playRadios(urlBuilder, Collections.singletonList(radioStation), this, new GsonResponseListener<QueueItem[]>() {
                @Override
                public void onResponse(QueueItem[] responseObject) {
                    // TODO: send event
                }
            });
            sendRequest(request);
        }
    }

    void fetchRadios() {
        setProgressVisible(true);
        final ApiUrlBuilder urlBuilder = getPiRemoteApplication().getApiUrlBuilder();
        RadioListRequest request = new RadioListRequest(urlBuilder, this, this);
        sendRequest(request);
    }

    void setProgressVisible(boolean visible) {
        if (isAdded()) {
            final int visibility = visible ? View.VISIBLE : View.GONE;
            getView().findViewById(android.R.id.progress).setVisibility(visibility);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchRadios();
    }

    @Override
    public void onResponse(RadioListResponse response) {
        setProgressVisible(false);
        if (response.getPrevious() == null) {
            adapter.clear();
        }
        final RadioStation[] items = response.getResults();
        adapter.addAll(Arrays.asList(items));
    }
}
