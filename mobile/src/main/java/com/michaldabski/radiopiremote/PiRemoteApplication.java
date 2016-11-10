package com.michaldabski.radiopiremote;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;

/**
 * Created by Michal on 30/10/2016.
 */

public class PiRemoteApplication extends Application {
    public static final String PREFERENCES_NAME = "pi-remote";
    public static final String PREF_ADDRESS = "address";
    private RequestQueue requestQueue;

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    public ApiUrlBuilder getApiUrlBuilder() {
        final String address = getSharedPreferences().getString(PREF_ADDRESS, "");
        return new ApiUrlBuilder(address);
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }
}
