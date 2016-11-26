package com.michaldabski.radiopiremote.tracks;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.Track;
import com.michaldabski.radiopiremote.api.models.TrackListResponse;
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
    @NonNull
    @Override
    protected Request<TrackListResponse> createRequest() {
        return new TrackListRequest(getUrlBuilder(), this, this);
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
        if (response.getPrevious() == null) {
            getAdapter().clear();
        }
        final Track[] items = response.getResults();
        getAdapter().addAll(Arrays.asList(items));
    }
}
