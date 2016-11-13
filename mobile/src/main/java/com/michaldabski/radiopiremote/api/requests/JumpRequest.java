package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.michaldabski.radiopiremote.api.ApiUrlBuilder;
import com.michaldabski.radiopiremote.api.events.QueueJumpEvent;
import com.michaldabski.radiopiremote.api.models.BaseMpdModel;
import com.michaldabski.radiopiremote.api.models.QueueItem;
import com.michaldabski.radiopiremote.api.models.RadioStation;
import com.michaldabski.radiopiremote.api.models.Track;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Michal on 31/10/2016.
 */

public class JumpRequest extends ApiRequest<QueueItem> {

    public static final String JUMP_TO_NEXT = "next";
    public static final String JUMP_TO_PREVIOUS = "previous";
    public static final String TYPE_TRACK = "track";
    public static final String TYPE_RADIO = "radio";

    /**
     * Build Jump request
     * @param urlBuilder url builder instance
     * @param toType String representing type of objec to jump to: track, radio, next or previous
     * @param listener success callback
     * @param responseListener error callback
     */
    public JumpRequest(ApiUrlBuilder urlBuilder, @NonNull String toType, Response.ErrorListener listener, @Nullable GsonResponseListener<QueueItem> responseListener) {
        super(Method.POST, urlBuilder, ApiUrlBuilder.ENDPOINT_JUMP + toType, QueueItem.class, listener, responseListener);
    }

    public static JumpRequest next(ApiUrlBuilder urlBuilder, Response.ErrorListener errorListener, GsonResponseListener<QueueItem> responseListener) {
        return new JumpRequest(urlBuilder, JUMP_TO_NEXT, errorListener, responseListener);
    }

    public static JumpRequest previous(ApiUrlBuilder urlBuilder, Response.ErrorListener errorListener, GsonResponseListener<QueueItem> responseListener) {
        return new JumpRequest(urlBuilder, JUMP_TO_PREVIOUS, errorListener, responseListener);
    }

    public static JumpRequest item(ApiUrlBuilder urlBuilder, @NonNull BaseMpdModel item, Response.ErrorListener errorListener, GsonResponseListener<QueueItem> responseListener) {
        final String toType;
        if (item instanceof Track) toType = TYPE_TRACK;
        else if (item instanceof RadioStation) toType = TYPE_RADIO;
        else throw new ClassCastException("item must be either radio station or track");

        // Use new item with the same id to avoid sending all other data
        item = new BaseMpdModel(item.getId());

        final JumpRequest request = new JumpRequest(urlBuilder, toType, errorListener, responseListener);
        request.setObject(item);
        return request;
    }

    @Override
    protected void deliverResponse(QueueItem response) {
        super.deliverResponse(response);
        EventBus.getDefault().post(new QueueJumpEvent(response));
    }
}
