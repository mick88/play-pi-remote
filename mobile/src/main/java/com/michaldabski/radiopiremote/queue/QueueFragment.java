package com.michaldabski.radiopiremote.queue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.michaldabski.radiopiremote.BaseFragment;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.ApiConfigurationError;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.events.QueueJumpEvent;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.QueueItem;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.JumpRequest;
import com.michaldabski.radiopiremote.api.requests.QueueRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Michal on 31/10/2016.
 */

public class QueueFragment extends BaseFragment implements GsonResponseListener, AdapterView.OnItemClickListener {
    ListView listView = null;
    private MpdItemAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final ImageLoader imageLoader = getPiRemoteApplication().getImageLoader();
        this.adapter = new MpdItemAdapter(getContext(), new ArrayList<BaseMpdModel>(), imageLoader);
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
                fetchQueue();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            fetchQueue();
        } catch (ApiConfigurationError e) {
            // not configured
        }
    }

    void setProgressVisible(boolean visible) {
        if (isAdded()) {
            final int visibility = visible ? View.VISIBLE : View.GONE;
            getView().findViewById(android.R.id.progress).setVisibility(visibility);
        }
    }

    @SuppressWarnings("unchecked")
    public void fetchQueue() {
        setProgressVisible(true);
        final ApiUrlBuilder urlBuilder = getPiRemoteApplication().getApiUrlBuilder();
        final QueueRequest<QueueItem[]> request = QueueRequest.getQueue(urlBuilder, this, this);
        sendRequest(request);
        final QueueRequest queueRequest = QueueRequest.getCurrentItem(urlBuilder, this, this);
        sendRequest(queueRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listView = null;
    }

    @Override
    public void onResponse(Object object) {
        if (object instanceof QueueItem) {
            final QueueItem queueItem = (QueueItem) object;
            adapter.setCurrentMpdId(queueItem.getMpdId());
        } else if (object instanceof QueueItem[]) {
            QueueItem[] queueItems = (QueueItem[]) object;
            if (listView != null) {
                adapter.clear();
                for (QueueItem queueItem : queueItems) {
                    final BaseMpdModel item = queueItem.getItem();
                    if (item != null) adapter.add(item);
                }
                setProgressVisible(false);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        setProgressVisible(false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final BaseMpdModel item = (BaseMpdModel) adapterView.getItemAtPosition(position);
        final ApiUrlBuilder urlBuilder = getPiRemoteApplication().getApiUrlBuilder();
        //noinspection unchecked
        final JumpRequest jumpRequest = JumpRequest.item(urlBuilder, item, this, this);
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
        if (adapter != null) {
            final QueueItem queueItem = event.getObject();
            adapter.setCurrentMpdId(queueItem.getMpdId());
        }
    }

    public static QueueFragment newInstance() {
        return new QueueFragment();
    }
}
