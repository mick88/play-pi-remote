package com.michaldabski.radiopiremote.api.requests;

/**
 * Created by Michal on 31/10/2016.
 */

public interface GsonResponseListener<T> {
    void onResponse(T responseObject);
}
