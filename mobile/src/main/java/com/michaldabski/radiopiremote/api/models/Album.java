package com.michaldabski.radiopiremote.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Michal on 31/10/2016.
 */

public class Album extends BaseModel {
    Artist artist;
    String name;
    int year;
    @SerializedName("art_url")
    String artUrl;

    public Artist getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getArtUrl() {
        return artUrl;
    }

    @Override
    public String toString() {
        return name;
    }
}
