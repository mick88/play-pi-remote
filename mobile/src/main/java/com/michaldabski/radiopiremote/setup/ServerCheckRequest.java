package com.michaldabski.radiopiremote.setup;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.models.Status;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.StatusRequest;

import java.util.Map;

/**
 * Created by Michal on 04/01/2017.
 */

public class ServerCheckRequest extends StatusRequest {
    public static final String HEADER_APP_NAME = "AppName";
    public static final String APP_NAME_PLAY_PI = "play_pi";

    public static class PlayPiNotInstalledException extends RuntimeException {
        public PlayPiNotInstalledException() {
            super("PlayPi is not installed on this server");
        }
    }

    public ServerCheckRequest(String address, Response.ErrorListener errorListener, @Nullable GsonResponseListener<Status> responseListener) {
        super(Method.GET, getUrlBuilder(address), errorListener, responseListener);
    }

    private static ApiUrlBuilder getUrlBuilder(String address) {
        return new ApiUrlBuilder(address);
    }

    @Override
    protected Response<Status> parseNetworkResponse(NetworkResponse response) {
        final Map<String, String> headers = response.headers;
        if (APP_NAME_PLAY_PI.equals(headers.get(HEADER_APP_NAME)) == false) {
            throw new PlayPiNotInstalledException();
        }
        return super.parseNetworkResponse(response);
    }
}
