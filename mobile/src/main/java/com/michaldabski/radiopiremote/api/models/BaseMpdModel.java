package com.michaldabski.radiopiremote.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Michal on 31/10/2016.
 */

public class BaseMpdModel extends BaseModel {
    @SerializedName("mpd_id")
    int mpdId;
    String name;

    public BaseMpdModel() {
        super();
    }

    public BaseMpdModel(int id) {
        super(id);
    }

    public int getMpdId() {
        return mpdId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
