package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;

/**
 * Created by Michal on 31/10/2016.
 */
public class ApiRequest<T> extends GsonRequest<T> {
    public ApiRequest(int method, ApiUrlBuilder urlBuilder, String endpoint, @Nullable Class<T> responseClass, Response.ErrorListener errorListener, @Nullable GsonResponseListener<T> responseListener) {
        super(method, urlBuilder.getEndpointUrl(endpoint), responseClass, errorListener, responseListener);
        setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1.5f));
    }
}
