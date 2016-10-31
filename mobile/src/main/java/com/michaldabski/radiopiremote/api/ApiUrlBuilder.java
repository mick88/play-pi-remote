package com.michaldabski.radiopiremote.api;

/** Defines urls for API endpoints
 * Created by Michal on 30/10/2016.
 */

public class ApiUrlBuilder {
    public static final String ENDPOINT_STATUS = "status/";
    public static final String ENDPOINT_JUMP = "jump/";
    public static final String ENDPOINT_QUEUE = "queue/";
    public static final String ENDPOINT_QUEUE_NOW_PLAYING = "queue/current";
    private static final String URL_TEMPLATE = "%sapi/%s";
    private final String ipAddress;


    public ApiUrlBuilder(String ipAddress) {
        this.ipAddress = ipAddress;
    }



    public String getEndpointUrl(String endpointName) {
        return String.format(URL_TEMPLATE, ipAddress, endpointName);
    }

    public String getStatusUrl() {
        return getEndpointUrl(ENDPOINT_STATUS);
    }
}
