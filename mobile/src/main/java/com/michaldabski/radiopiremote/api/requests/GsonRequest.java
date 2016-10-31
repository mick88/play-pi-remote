package com.michaldabski.radiopiremote.api.requests;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Base request implementation for Gson requests
 * Created by Michal on 31/10/2016.
 */
public class GsonRequest<T> extends Request<T> {
    private static final Gson GSON = new Gson();
    public static final String CHARSET_NAME = "utf-8";
    public static final String TAG = "GsonRequest";
    private final GsonResponseListener<T> listener;
    private final Class<T> responseClass;
    /**
     * Object to be send by POST or PUT
     */
    @Nullable
    private Object object;

    public GsonRequest(int method, String url, @Nullable Class<T> responseClass, Response.ErrorListener listener, @Nullable GsonResponseListener<T> responseListener) {
        super(method, url, listener);
        this.listener = responseListener;
        this.responseClass = responseClass;
        Log.d(TAG, getUrl());
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            final T object = getResponseObject(response);
            final Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
            return Response.success(object, cacheEntry);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }
    }

    private T getResponseObject(NetworkResponse response) throws IOException {
        if (this.responseClass == null) return null;
        ByteArrayInputStream stream = new ByteArrayInputStream(response.data);
        final String charset = HttpHeaderParser.parseCharset(response.headers, "utf-8");
        InputStreamReader reader = new InputStreamReader(stream, charset);
        final T object = GSON.fromJson(reader, responseClass);
        reader.close();
        return object;
    }

    @Override
    protected void deliverResponse(T response) {
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (object == null) {
            return super.getBody();
        } else {
            final String json = GSON.toJson(object);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, json);
            }
            try {
                return json.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        try {
            final NetworkResponse networkResponse = volleyError.networkResponse;
            if (networkResponse != null) {
                final String response = new String(networkResponse.data, CHARSET_NAME);
                Log.e(TAG, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.parseNetworkError(volleyError);
    }

    /**
     * Sets object to be attached to thie request
     */
    public void setObject(@Nullable Object object) {
        this.object = object;
    }
}
