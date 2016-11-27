package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.QueueItem;
import com.michaldabski.radiopiremote.api.models.QueueResponse;

/**
 * Created by Michal on 31/10/2016.
 */

public class QueueRequest<T> extends ApiRequest<T> {
    public QueueRequest(int method, ApiUrlBuilder urlBuilder, String endpoint, @Nullable Class<T> responseClass, Response.ErrorListener errorListener, @Nullable GsonResponseListener<T> responseListener) {
        super(method, urlBuilder, endpoint, responseClass, errorListener, responseListener);
    }

    public static QueueRequest<QueueItem[]> getQueueItems(ApiUrlBuilder apiUrlBuilder, Response.ErrorListener errorListener, GsonResponseListener<QueueItem[]> responseListener) {
        return new QueueRequest<>(Method.GET, apiUrlBuilder, ApiUrlBuilder.ENDPOINT_QUEUE_ITEMS, QueueItem[].class, errorListener, responseListener);
    }

    public static QueueRequest<QueueResponse> getQueue(ApiUrlBuilder apiUrlBuilder, Response.ErrorListener errorListener, GsonResponseListener<QueueResponse> responseListener) {
        return new QueueRequest<>(Method.GET, apiUrlBuilder, ApiUrlBuilder.ENDPOINT_QUEUE, QueueResponse.class, errorListener, responseListener);
    }

    public static QueueRequest<QueueItem> getCurrentItem(ApiUrlBuilder apiUrlBuilder, Response.ErrorListener errorListener, GsonResponseListener<QueueItem> responseListener) {
        return new QueueRequest<>(Method.GET, apiUrlBuilder, ApiUrlBuilder.ENDPOINT_QUEUE_NOW_PLAYING, QueueItem.class, errorListener, responseListener);
    }

    public static QueueRequest<QueueResponse> removeQueueItem(ApiUrlBuilder apiUrlBuilder, @NonNull BaseMpdModel item, Response.ErrorListener errorListener, GsonResponseListener<QueueResponse> responseListener) {
        return new QueueRequest<>(Method.DELETE, apiUrlBuilder, ApiUrlBuilder.ENDPOINT_QUEUE + item.getMpdId() + '/', QueueResponse.class, errorListener, responseListener);
    }
}
