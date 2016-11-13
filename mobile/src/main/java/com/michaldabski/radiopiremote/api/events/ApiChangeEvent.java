package com.michaldabski.radiopiremote.api.events;

/**
 * Created by Michal on 13/11/2016.
 */

public class ApiChangeEvent<T> {
    final T object;

    public ApiChangeEvent(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
