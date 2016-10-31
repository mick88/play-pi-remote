package com.michaldabski.radiopiremote.api.models;


/**
 * Base class for json representations of Django models
 *
 * Created by Michal on 31/10/2016.
 */
public class BaseModel {
    int id;

    public BaseModel() {
    }

    public BaseModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseModel baseModel = (BaseModel) o;

        return id == baseModel.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
