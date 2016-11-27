package com.michaldabski.radiopiremote.api.models;

/**
 * Created by Michal on 27/11/2016.
 */

public class QueueResponse {
    QueueItem current;
    QueueItem[] items;

    public QueueItem getCurrent() {
        return current;
    }

    public QueueItem[] getItems() {
        return items;
    }
}
