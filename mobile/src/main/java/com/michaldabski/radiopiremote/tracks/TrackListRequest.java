package com.michaldabski.radiopiremote.tracks;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.TrackListResponse;
import com.michaldabski.radiopiremote.api.requests.GsonRequest;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;

/**
 * Created by Michal on 21/11/2016.
 */

public class TrackListRequest extends GsonRequest<TrackListResponse> {
    public TrackListRequest(ApiUrlBuilder urlBuilder, Response.ErrorListener listener, @Nullable GsonResponseListener<TrackListResponse> responseListener) {
        super(Method.GET, urlBuilder.getEndpointUrl(ApiUrlBuilder.ENDPOINT_TRACKS), TrackListResponse.class, listener, responseListener);
    }
}
