package com.michaldabski.radiopiremote;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Michal on 31/10/2016.
 */

public class BaseFragment extends Fragment implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        String message = error.getMessage();
        if (message == null) {
            message = "Network error";
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public PiRemoteApplication getPiRemoteApplication() {
        return ((PiRemoteApplication) getActivity().getApplication());
    }

    protected void sendRequest(Request<?> request) {
        request.setTag(this);
        getPiRemoteApplication().getRequestQueue().add(request);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getPiRemoteApplication().getRequestQueue().cancelAll(this);
    }
}
