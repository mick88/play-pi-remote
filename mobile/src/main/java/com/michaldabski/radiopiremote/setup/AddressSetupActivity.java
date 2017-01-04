package com.michaldabski.radiopiremote.setup;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michaldabski.radiopiremote.PiRemoteApplication;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.models.Status;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.base.BaseActivity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

/**
 * Created by Michal on 10/11/2016.
 */

public class AddressSetupActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        findViewById(R.id.btnOk).setOnClickListener(this);
        findViewById(R.id.btnDiscover).setOnClickListener(this);
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

            case R.id.btnDiscover:
                try {
                    discoverNetworkDevices();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void discoverNetworkDevices() throws SocketException {
        final View btnDiscovery = findViewById(R.id.btnDiscover);
        btnDiscovery.setEnabled(false);

        for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
                if (!inetAddress.isLoopbackAddress()) {
                    String ipAddress =  inetAddress.getHostAddress();
                    final String[] split = ipAddress.split(".");
                    if (split.length < 4) continue;
                    final String addressStart = String.format("%s.%s.%s.", split[0], split[1], split[2]);
                    for (int i = 1; i < 255; i++) {
                        final String address = addressStart + i;
                        testAddress(address);
                        Log.d("discoverNetworkDevices", "Testing " + address);
                    }
                }
            }
        }
    }
}
