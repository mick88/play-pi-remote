package com.michaldabski.radiopiremote.tracks;

import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.michaldabski.radiopiremote.api.models.Track;
import com.michaldabski.radiopiremote.api.models.TrackListResponse;
import com.michaldabski.radiopiremote.api.requests.EnqueueRequest;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.PlayRequest;
import com.michaldabski.radiopiremote.base.BaseApiFragment;
import com.michaldabski.radiopiremote.queue.MpdItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michal on 26/11/2016.
 */

public class TrackListFragment extends BaseApiFragment<TrackListResponse, BaseMpdModel> {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mpd_list, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @NonNull
    @Override
    protected Request<TrackListResponse> createRequest(int page) {
        final ApiUrlBuilder urlBuilder = getUrlBuilder();
        return new TrackListRequest(urlBuilder.getTracksUrl(page, this.search), this, this);
    }

    @NonNull
    @Override
    protected ArrayAdapter<BaseMpdModel> createAdapter() {
        final ImageLoader imageLoader = getPiRemoteApplication().getImageLoader();
        return new MpdItemAdapter(getContext(), new ArrayList<BaseMpdModel>(50), imageLoader);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Object item = parent.getItemAtPosition(position);
        if (item instanceof Track) {
            Track track = new Track(((Track) item).getId());
            final List<Track> tracks = Collections.singletonList(track);
            PlayRequest request = PlayRequest.playTracks(getUrlBuilder(), tracks, this, null);
            sendRequest(request);
        }
    }

    public static TrackListFragment newInstance() {
        return new TrackListFragment();
    }

    @Override
    public void onResponse(TrackListResponse response) {
        super.onResponse(response);
        final Track[] items = response.getResults();
        getAdapter().addAll(Arrays.asList(items));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Object item = adapterView.getItemAtPosition(position);
        if (item instanceof Track) {
            QueueItem queueItem = new QueueItem(((Track) item));
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
