package com.michaldabski.radiopiremote.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.michaldabski.radiopiremote.BaseActivity;
import com.michaldabski.radiopiremote.BuildConfig;
import com.michaldabski.radiopiremote.PiRemoteApplication;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.models.Status;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.StatusRequest;
import com.michaldabski.radiopiremote.control.PlaybackControlFragment;
import com.michaldabski.radiopiremote.queue.QueueFragment;
import com.michaldabski.radiopiremote.setup.AddressSetupActivity;

/**
 * Created by Michal on 30/10/2016.
 */

public class MainActivity extends BaseActivity implements GsonResponseListener<Status> {

    public static final int REQUEST_CODE_SETUP = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Version " + BuildConfig.VERSION_NAME);

        final SharedPreferences preferences = getPiRemoteApplication().getSharedPreferences();
        if (savedInstanceState == null && preferences.contains(PiRemoteApplication.PREF_ADDRESS) == false) {
            Intent intent = new Intent(this, AddressSetupActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SETUP);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETUP:
                if (resultCode == RESULT_OK) {
                    getPlaybackControlsFragment().fetchStatus();
                    final QueueFragment queueFragment = (QueueFragment) getSupportFragmentManager().findFragmentByTag("queue-fragment");
                    if (queueFragment != null) queueFragment.fetchQueue();
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOpenInBrowser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                final String address = getPiRemoteApplication().getSharedPreferences().getString(PiRemoteApplication.PREF_ADDRESS, null);
                intent.setData(Uri.parse(address));
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Override KEY DOWN for volume keys
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            String volumeDelta = null;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    volumeDelta = PlaybackControlFragment.VOLUME_INCREMENT_VALUE;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (volumeDelta == null) {
                        volumeDelta = PlaybackControlFragment.VOLUME_DECREMENT_VALUE;
                    }
                    Status status = new Status();
                    status.setVolume(volumeDelta);
                    final StatusRequest request = StatusRequest.post(getPiRemoteApplication().getApiUrlBuilder(), status, this, this);
                    sendRequest(request);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onResponse(Status object) {
        final PlaybackControlFragment fragment = getPlaybackControlsFragment();
        if (fragment != null) {
            fragment.updateStatus(object);
        }
    }

    public PlaybackControlFragment getPlaybackControlsFragment() {
        return (PlaybackControlFragment) getSupportFragmentManager().findFragmentByTag("playback-controls");
    }
}
