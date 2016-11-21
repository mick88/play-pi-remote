package com.michaldabski.radiopiremote.api.models;

/**
 * Created by Michal on 21/11/2016.
 */

public class ListResponse<T> {
    int count;
    String next, previous;
    T [] results;

    public T[] getResults() {
        return results;
    }

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }
}
