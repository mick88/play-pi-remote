package com.michaldabski.radiopiremote.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Michal on 31/10/2016.
 */

public class QueueItem {
    @SerializedName("mpd_id")
    int mpdId;
    Track track;
    @SerializedName("radio_station")
    RadioStation radioStation;

    /**
     * Gets either the radio statio or track, depending which one is not null
     */
    public BaseMpdModel getItem() {
        return track == null ? radioStation : track;
    }

    public int getMpdId() {
        return mpdId;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getItem(), mpdId);
    }

    public QueueItem(RadioStation radioStation) {
        this.radioStation = radioStation;
    }

    public QueueItem(Track track) {

        this.track = track;
    }
}
