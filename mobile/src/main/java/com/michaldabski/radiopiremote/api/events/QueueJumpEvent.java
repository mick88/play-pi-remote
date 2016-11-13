package com.michaldabski.radiopiremote.api.events;

import com.michaldabski.radiopiremote.api.models.QueueItem;

/**
 * Created by Michal on 13/11/2016.
 */

public class QueueJumpEvent extends ApiChangeEvent<QueueItem> {
    public QueueJumpEvent(QueueItem object) {
        super(object);
    }
}
