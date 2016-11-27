package com.michaldabski.radiopiremote.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.michaldabski.radiopiremote.PiRemoteApplication;
import com.michaldabski.radiopiremote.R;
import com.michaldabski.radiopiremote.api.models.Status;
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener;
import com.michaldabski.radiopiremote.api.requests.StatusRequest;
import com.michaldabski.radiopiremote.base.BaseActivity;
import com.michaldabski.radiopiremote.control.PlaybackControlFragment;
import com.michaldabski.radiopiremote.queue.QueueFragment;
import com.michaldabski.radiopiremote.radios.RadioListFragment;
import com.michaldabski.radiopiremote.setup.AddressSetupActivity;
import com.michaldabski.radiopiremote.tracks.TrackListFragment;

/**
 * Created by Michal on 30/10/2016.
 */

public class MainActivity extends BaseActivity implements GsonResponseListener<Status>, NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE_SETUP = 1;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        setupActionBar();
        setupNavDrawer();

        final SharedPreferences preferences = getPiRemoteApplication().getSharedPreferences();
        if (savedInstanceState == null && preferences.contains(PiRemoteApplication.PREF_ADDRESS) == false) {
            launchSetupActivity();
        }

        if (savedInstanceState == null) {
            final QueueFragment fragment = QueueFragment.newInstance();
            showFragment(fragment);
        }
    }

    void launchSetupActivity() {
        Intent intent = new Intent(this, AddressSetupActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETUP);
    }

    void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    void setupNavDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETUP:
                if (resultCode == RESULT_OK) {
                    getPlaybackControlsFragment().fetchStatus();
                    final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                    if (fragment instanceof QueueFragment) {
                        ((QueueFragment) fragment).sendRequest();
                    }
                } else {
                    final SharedPreferences preferences = getPiRemoteApplication().getSharedPreferences();
                    if (preferences.contains(PiRemoteApplication.PREF_ADDRESS) == false) {
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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

    void showFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getGroupId() == R.id.groupPages) {
            item.setChecked(true);
        }
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.menuQueue:
                final QueueFragment fragment = QueueFragment.newInstance();
                showFragment(fragment);
                return true;

            case R.id.menuRadios:
                final RadioListFragment radioListFragment = RadioListFragment.newInstance();
                showFragment(radioListFragment);
                return true;

            case R.id.menuTracks:
                final TrackListFragment trackListFragment = TrackListFragment.newInstance();
                showFragment(trackListFragment);
                return true;

            case R.id.menuSettings:
                launchSetupActivity();
                return true;

            case R.id.menuOpenInBrowser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                final String address = getPiRemoteApplication().getSharedPreferences().getString(PiRemoteApplication.PREF_ADDRESS, null);
                intent.setData(Uri.parse(address));
                startActivity(intent);
                return true;
        }

        return false;
    }
}
