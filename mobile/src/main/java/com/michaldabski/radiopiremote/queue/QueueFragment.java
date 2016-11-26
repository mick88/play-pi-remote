package com.michaldabski.radiopiremote.queue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.events.QueueJumpEvent;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.QueueItem;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.JumpRequest;
import com.michaldabski.radiopiremote.api.requests.QueueRequest;
import com.michaldabski.radiopiremote.base.BaseApiFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Michal on 31/10/2016.
 */

public class QueueFragment extends BaseApiFragment<QueueItem[], BaseMpdModel> {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    protected ArrayAdapter<BaseMpdModel> createAdapter() {
        final ImageLoader imageLoader = getPiRemoteApplication().getImageLoader();
        return new MpdItemAdapter(getContext(), new ArrayList<BaseMpdModel>(), imageLoader);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.queue_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                sendRequest();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sendRequest() {
        super.sendRequest();

        final QueueRequest queueRequest = QueueRequest.getCurrentItem(getUrlBuilder(), this, new GsonResponseListener<QueueItem>() {
            @Override
            public void onResponse(QueueItem responseObject) {
                final int mpdId = responseObject.getMpdId();
                ((MpdItemAdapter) getAdapter()).setCurrentMpdId(mpdId);
            }
        });
        sendRequest(queueRequest);
    }

    @NonNull
    @Override
    protected Request<QueueItem[]> createRequest(int page) {
        final ApiUrlBuilder urlBuilder = getUrlBuilder();
        final QueueRequest<QueueItem[]> request = QueueRequest.getQueue(urlBuilder, this, this);
        return request;
    }

    @Override
    public void onResponse(QueueItem[] queueItems) {
        super.onResponse(queueItems);
        final ArrayAdapter<BaseMpdModel> adapter = getAdapter();
        adapter.clear();
        for (QueueItem queueItem : queueItems) {
            final BaseMpdModel item = queueItem.getItem();
            if (item != null) adapter.add(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final BaseMpdModel item = (BaseMpdModel) adapterView.getItemAtPosition(position);
        final ApiUrlBuilder urlBuilder = getUrlBuilder();
        //noinspection unchecked
        final JumpRequest jumpRequest = JumpRequest.item(urlBuilder, item, this, null);
        sendRequest(jumpRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onQueueChange(QueueJumpEvent event) {
        final QueueItem queueItem = event.getObject();
        ((MpdItemAdapter) getAdapter()).setCurrentMpdId(queueItem.getMpdId());
    }

    public static QueueFragment newInstance() {
        return new QueueFragment();
    }
}
