package com.michaldabski.radiopiremote.api.events;

import com.michaldabski.radiopiremote.api.models.Status;

/**
 * Created by Michal on 13/11/2016.
 */

public class StatusChangeEvent extends ApiChangeEvent<Status> {

    public StatusChangeEvent(Status status) {
        super(status);
    }
}
