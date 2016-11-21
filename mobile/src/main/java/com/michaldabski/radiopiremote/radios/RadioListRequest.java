package com.michaldabski.radiopiremote.radios;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.RadioListResponse;
import com.michaldabski.radiopiremote.api.requests.GsonRequest;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;

/**
 * Created by Michal on 21/11/2016.
 */

public class RadioListRequest extends GsonRequest<RadioListResponse> {
    public RadioListRequest(ApiUrlBuilder urlBuilder, Response.ErrorListener listener, @Nullable GsonResponseListener<RadioListResponse> responseListener) {
        super(Method.GET, urlBuilder.getEndpointUrl(ApiUrlBuilder.ENDPOINT_RADIOS), RadioListResponse.class, listener, responseListener);
    }
}
