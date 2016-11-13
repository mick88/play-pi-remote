package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.events.StatusChangeEvent;
import com.michaldabski.radiopiremote.api.models.Status;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Michal on 31/10/2016.
 */

public class StatusRequest extends ApiRequest<Status> {
    public StatusRequest(int method, ApiUrlBuilder urlBuilder, Response.ErrorListener errorListener, @Nullable GsonResponseListener<Status> responseListener) {
        super(method, urlBuilder, ApiUrlBuilder.ENDPOINT_STATUS, Status.class, errorListener, responseListener);
    }

    /**
     * Shortcut to make a request to fetch status
     */
    public static StatusRequest get(ApiUrlBuilder urlBuilder, Response.ErrorListener errorListener, @Nullable GsonResponseListener<Status> responseListener) {
        return new StatusRequest(Method.GET, urlBuilder, errorListener, responseListener);
    }

    /**
     * Shortcut to build a request to POST status update
     */
    public static StatusRequest post(ApiUrlBuilder urlBuilder, Status status, Response.ErrorListener errorListener, @Nullable GsonResponseListener<Status> responseListener) {
        final StatusRequest request = new StatusRequest(Method.POST, urlBuilder, errorListener, responseListener);
        request.setObject(status);
        return request;
    }

    @Override
    protected void deliverResponse(Status response) {
        super.deliverResponse(response);
        switch (getMethod()) {
            case Method.POST:
            case Method.PUT:
            case Method.PATCH:
            case Method.DELETE:
                EventBus.getDefault().post(new StatusChangeEvent(response));
                break;
        }
    }
}
