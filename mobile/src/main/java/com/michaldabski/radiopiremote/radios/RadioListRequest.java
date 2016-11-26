package com.michaldabski.radiopiremote.radios;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.models.RadioListResponse;
import com.michaldabski.radiopiremote.api.requests.GsonRequest;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;

/**
 * Created by Michal on 21/11/2016.
 */

public class RadioListRequest extends GsonRequest<RadioListResponse> {
    public RadioListRequest(@NonNull String url, Response.ErrorListener listener, @Nullable GsonResponseListener<RadioListResponse> responseListener) {
        super(Method.GET, url, RadioListResponse.class, listener, responseListener);
    }
}
