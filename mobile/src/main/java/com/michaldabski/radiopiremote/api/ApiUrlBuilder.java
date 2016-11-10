package com.michaldabski.radiopiremote.api;

import android.text.TextUtils;

/** Defines urls for API endpoints
 * Created by Michal on 30/10/2016.
 */

public class ApiUrlBuilder {
    public static final String ENDPOINT_STATUS = "status/";
    public static final String ENDPOINT_JUMP = "jump/";
    public static final String ENDPOINT_QUEUE = "queue/";
    public static final String ENDPOINT_QUEUE_NOW_PLAYING = "queue/current";
    private static final String URL_TEMPLATE = "%sapi/%s";
    private final String address;


    public ApiUrlBuilder(String address) throws ApiConfigurationError {
        if (TextUtils.isEmpty(address)) throw new ApiConfigurationError("Address is not configured");
        this.address = address;
    }



    public String getEndpointUrl(String endpointName) {
        return String.format(URL_TEMPLATE, address, endpointName);
    }

    public String getStatusUrl() {
        return getEndpointUrl(ENDPOINT_STATUS);
    }
}
