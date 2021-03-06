package com.michaldabski.radiopiremote.api;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/** Defines urls for API endpoints
 * Created by Michal on 30/10/2016.
 */

public class ApiUrlBuilder {
    public static final String
            PLAY_RADIOS = "radios",
            PLAY_TRACKS = "tracks";
    public static final String ENDPOINT_STATUS = "status/";
    public static final String ENDPOINT_JUMP = "jump/";
    public static final String ENDPOINT_QUEUE = "queue/";
    public static final String ENDPOINT_QUEUE_ITEMS = "queue/items";
    public static final String ENDPOINT_QUEUE_NOW_PLAYING = "queue/current";
    public static final String ENDPOINT_TRACKS = "tracks/";
    public static final String ENDPOINT_RADIOS = "radio_stations/";
    public static final String ENDPOINT_PLAY = "play/";
    private static final String URL_TEMPLATE = "%sapi/%s";
    private final String address;


    public ApiUrlBuilder(String address) throws ApiConfigurationError {
        if (TextUtils.isEmpty(address)) throw new ApiConfigurationError("Address is not configured");
        this.address = address;
    }

    public String getPlayUrl(String type) {
        return getEndpointUrl(ENDPOINT_PLAY) + type;
    }

    public String getEndpointUrl(String endpointName) {
        return String.format(URL_TEMPLATE, address, endpointName);
    }

    public String getStatusUrl() {
        return getEndpointUrl(ENDPOINT_STATUS);
    }

    public String getTracksUrl(int page, @Nullable String search) {
        String url = getEndpointUrl(ApiUrlBuilder.ENDPOINT_TRACKS);
        url += "?page=" + page;
        if (search != null) {
            url += "&search=" + search;
        }
        return url;
    }

    public String getRadiosUrl(int page, @Nullable String search) {
        String url = getEndpointUrl(ApiUrlBuilder.ENDPOINT_RADIOS);
        url += "?page=" + page;
        if (search != null) {
            url += "&search=" + search;
        }
        return url;
    }
}
