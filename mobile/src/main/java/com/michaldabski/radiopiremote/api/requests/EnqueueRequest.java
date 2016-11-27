package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.QueueItem;

/**
 * Created by Michal on 21/11/2016.
 */

public class EnqueueRequest extends GsonRequest<QueueItem[]> {
    public EnqueueRequest(ApiUrlBuilder urlBuilder, QueueItem item, Response.ErrorListener listener, @Nullable GsonResponseListener<QueueItem[]> responseListener) {
        super(Method.POST, urlBuilder.getEndpointUrl(ApiUrlBuilder.ENDPOINT_QUEUE), QueueItem[].class, listener, responseListener);
        setObject(item);
    }
}
