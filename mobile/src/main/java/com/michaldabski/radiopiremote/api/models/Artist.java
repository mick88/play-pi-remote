package com.michaldabski.radiopiremote.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Michal on 31/10/2016.
 */

public class Artist extends BaseModel {
    String name;
    @SerializedName("art_url")
    String artUrl;

    public String getArtUrl() {
        return artUrl;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
