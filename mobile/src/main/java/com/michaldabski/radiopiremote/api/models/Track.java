package com.michaldabski.radiopiremote.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Michal on 31/10/2016.
 */

public class Track extends BaseMpdModel {
    Artist artist;
    Album album;
    @SerializedName("stream_id")
    String streamId;
    @SerializedName("track_no")
    int trackNo;
    int rating;

    @Override
    public String toString() {
        return String.format("%s - %s", artist, name);
    }

    public Artist getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }

    public String getStreamId() {
        return streamId;
    }

    public int getTrackNo() {
        return trackNo;
    }

    public int getRating() {
        return rating;
    }

    public Track(int id) {
        super(id);
    }
}
