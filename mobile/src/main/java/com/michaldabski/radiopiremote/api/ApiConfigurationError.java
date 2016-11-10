package com.michaldabski.radiopiremote.api;

/**
 * Created by Michal on 10/11/2016.
 */

public class ApiConfigurationError extends RuntimeException {
    public ApiConfigurationError(String message) {
        super(message);
    }
}
