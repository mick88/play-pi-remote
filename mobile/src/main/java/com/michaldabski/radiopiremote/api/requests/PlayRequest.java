package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.QueueItem;
import com.michaldabski.radiopiremote.api.models.RadioStation;

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
}
