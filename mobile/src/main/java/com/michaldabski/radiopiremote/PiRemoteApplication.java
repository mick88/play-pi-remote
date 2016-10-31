package com.michaldabski.radiopiremote;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;

/**
 * Created by Michal on 30/10/2016.
 */

public class PiRemoteApplication extends Application {
    private RequestQueue requestQueue;
    private ApiUrlBuilder apiUrlBuilder;

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    public ApiUrlBuilder getApiUrlBuilder() {
        if (apiUrlBuilder == null) {
            apiUrlBuilder = new ApiUrlBuilder(BuildConfig.HOST_ADDRESS);
        }
        return apiUrlBuilder;
    }
}
