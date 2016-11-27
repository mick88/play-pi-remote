package com.michaldabski.radiopiremote.api.models;

import android.support.annotation.Nullable;

/**
 * Created by Michal on 27/11/2016.
 */

public class QueueResponse {
    @Nullable
    QueueItem current;
    QueueItem[] items;

    @Nullable
    public QueueItem getCurrent() {
        return current;
    }

    public QueueItem[] getItems() {
        return items;
    }
}
