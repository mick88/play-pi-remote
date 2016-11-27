package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.QueueItem;
import com.michaldabski.radiopiremote.api.models.QueueResponse;

/**
 * Created by Michal on 21/11/2016.
 */

public class EnqueueRequest extends GsonRequest<QueueResponse> {
    public EnqueueRequest(ApiUrlBuilder urlBuilder, QueueItem item, Response.ErrorListener listener, @Nullable GsonResponseListener<QueueResponse> responseListener) {
        super(Method.POST, urlBuilder.getEndpointUrl(ApiUrlBuilder.ENDPOINT_QUEUE), QueueResponse.class, listener, responseListener);
        setObject(item);
    }
}
