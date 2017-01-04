package com.michaldabski.radiopiremote.setup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michaldabski.radiopiremote.PiRemoteApplication;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.models.Status;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.base.BaseActivity;

/**
 * Created by Michal on 10/11/2016.
 */

public class AddressSetupActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        findViewById(R.id.btnOk).setOnClickListener(this);
    }

    void testAddress(@NonNull String address) {
        if (address.contains("://") == false) {
            address = "http://" + address;
        }

        if (address.endsWith("/") == false) {
            address += '/';
        }

        final String ipAddress = address;
        final ServerCheckRequest request = new ServerCheckRequest(ipAddress, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final EditText editAddress = (EditText) findViewById(R.id.editAddress);
                editAddress.setError(getString(R.string.address_invalid));
                editAddress.requestFocus();
            }
        }, new GsonResponseListener<Status>() {
            @Override
            public void onResponse(Status responseObject) {
                getPiRemoteApplication().getSharedPreferences().edit().putString(PiRemoteApplication.PREF_ADDRESS, ipAddress).apply();
                setResult(RESULT_OK);
                finish();
            }
        });
        sendRequest(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                final EditText editAddress = (EditText) findViewById(R.id.editAddress);
                editAddress.setError(null);
                final String address = editAddress.getText().toString();
                testAddress(address.trim());
                break;
        }
    }
}
