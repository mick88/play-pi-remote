package com.michaldabski.radiopiremote;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.michaldabski.radiopiremote.api.ApiConfigurationError;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.utils.BitmapCache;

/**
 * Created by Michal on 30/10/2016.
 */

public class PiRemoteApplication extends Application {
    public static final String PREFERENCES_NAME = "pi-remote";
    public static final String PREF_ADDRESS = "address";
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(getRequestQueue(), new BitmapCache());
        }
        return imageLoader;
    }

    public ApiUrlBuilder getApiUrlBuilder() throws ApiConfigurationError {
        final String address = getSharedPreferences().getString(PREF_ADDRESS, "");
        return new ApiUrlBuilder(address);
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }
}
