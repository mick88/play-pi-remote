package com.michaldabski.radiopiremote.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michaldabski.radiopiremote.PiRemoteApplication;

/**
 * Created by Michal on 31/10/2016.
 */

public class BaseActivity extends AppCompatActivity implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        String message = error.getMessage();
        if (message == null) {
            message = "Network error";
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public PiRemoteApplication getPiRemoteApplication() {
        return ((PiRemoteApplication) getApplication());
    }

    protected void sendRequest(Request<?> request) {
        request.setTag(this);
        getPiRemoteApplication().getRequestQueue().add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPiRemoteApplication().getRequestQueue().cancelAll(this);
    }
}
