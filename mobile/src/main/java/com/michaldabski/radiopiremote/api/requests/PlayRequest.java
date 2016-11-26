package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.events.QueueJumpEvent;
import com.michaldabski.radiopiremote.api.models.QueueItem;
import com.michaldabski.radiopiremote.api.models.RadioStation;
import com.michaldabski.radiopiremote.api.models.Track;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Michal on 21/11/2016.
 */

public class PlayRequest extends GsonRequest<QueueItem[]> {
    public PlayRequest(String url, Response.ErrorListener listener, @Nullable GsonResponseListener<QueueItem[]> responseListener) {
        super(Method.POST, url, QueueItem[].class, listener, responseListener);
    }

    public static PlayRequest playRadios(ApiUrlBuilder urlBuilder, List<RadioStation> radioStations, Response.ErrorListener listener, @Nullable GsonResponseListener<QueueItem[]> responseListener) {
        final String url = urlBuilder.getPlayUrl(ApiUrlBuilder.PLAY_RADIOS);
        final PlayRequest request = new PlayRequest(url, listener, responseListener);
        request.setObject(radioStations);
        return request;
    }

    public static PlayRequest playTracks(ApiUrlBuilder urlBuilder, List<Track> radioStations, Response.ErrorListener listener, @Nullable GsonResponseListener<QueueItem[]> responseListener) {
        final String url = urlBuilder.getPlayUrl(ApiUrlBuilder.PLAY_TRACKS);
        final PlayRequest request = new PlayRequest(url, listener, responseListener);
        request.setObject(radioStations);
        return request;
    }

    @Override
    protected void deliverResponse(QueueItem[] response) {
        super.deliverResponse(response);
        QueueItem item = null;
        if (response.length > 0) {
            item = response[0];
        }
        EventBus.getDefault().post(new QueueJumpEvent(item));
    }
}
