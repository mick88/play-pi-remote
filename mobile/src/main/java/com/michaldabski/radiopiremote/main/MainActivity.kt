package com.michaldabski.radiopiremote.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.KeyEvent
import android.view.MenuItem

import com.michaldabski.radiopiremote.PiRemoteApplication
import com.michaldabski.radiopiremote.R
import com.michaldabski.radiopiremote.api.models.Status
import com.michaldabski.radiopiremote.api.requests.GsonResponseListener
import com.michaldabski.radiopiremote.api.requests.StatusRequest
import com.michaldabski.radiopiremote.base.BaseActivity
import com.michaldabski.radiopiremote.control.PlaybackControlFragment
import com.michaldabski.radiopiremote.queue.QueueFragment
import com.michaldabski.radiopiremote.radios.RadioListFragment
import com.michaldabski.radiopiremote.setup.AddressSetupActivity
import com.michaldabski.radiopiremote.tracks.TrackListFragment

/**
 * Created by Michal on 30/10/2016.
 */

class MainActivity : BaseActivity(), GsonResponseListener<Status>, NavigationView.OnNavigationItemSelectedListener {
    private var drawerLayout: DrawerLayout? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null

    val playbackControlsFragment: PlaybackControlFragment?
        get() = supportFragmentManager.findFragmentByTag("playback-controls") as PlaybackControlFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_drawer)

        setupActionBar()
        setupNavDrawer()

        val preferences = piRemoteApplication.sharedPreferences
        if (savedInstanceState == null && preferences.contains(PiRemoteApplication.PREF_ADDRESS) == false) {
            launchSetupActivity()
        }

        if (savedInstanceState == null) {
            val fragment = QueueFragment.newInstance()
            showFragment(fragment)
        }
    }

    internal fun launchSetupActivity() {
        val intent = Intent(this, AddressSetupActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SETUP)
    }

    internal fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    internal fun setupNavDrawer() {
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.getItem(0).isChecked = true

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer, R.string.closeDrawer)
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SETUP -> if (resultCode == RESULT_OK) {
                playbackControlsFragment!!.fetchStatus()
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
                (fragment as? QueueFragment)?.sendRequest()
            } else {
                val preferences = piRemoteApplication.sharedPreferences
                if (preferences.contains(PiRemoteApplication.PREF_ADDRESS) == false) {
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return actionBarDrawerToggle!!.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Override KEY DOWN for volume keys
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    val volumeDelta = PlaybackControlFragment.VOLUME_INCREMENT_VALUE
                    val status = Status()
                    status.volume = volumeDelta
                    val request = StatusRequest.post(piRemoteApplication.apiUrlBuilder, status, this, this)
                    sendRequest(request)
                    return true
                }
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    val volumeDelta = PlaybackControlFragment.VOLUME_DECREMENT_VALUE
                    val status = Status()
                    status.volume = volumeDelta
                    val request = StatusRequest.post(piRemoteApplication.apiUrlBuilder, status, this, this)
                    sendRequest(request)
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onResponse(status: Status) {
        val fragment = playbackControlsFragment
        fragment?.updateStatus(status)
    }

    protected fun showFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.groupId == R.id.groupPages) {
            item.isChecked = true
        }
        drawerLayout!!.closeDrawers()
        return when (item.itemId) {
            R.id.menuQueue -> {
                val fragment = QueueFragment.newInstance()
                showFragment(fragment)
                return true
            }

            R.id.menuRadios -> {
                val radioListFragment = RadioListFragment.newInstance()
                showFragment(radioListFragment)
                return true
            }

            R.id.menuTracks -> {
                val trackListFragment = TrackListFragment.newInstance()
                showFragment(trackListFragment)
                return true
            }

            R.id.menuSettings -> {
                launchSetupActivity()
                return true
            }

            R.id.menuOpenInBrowser -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val address = piRemoteApplication.sharedPreferences.getString(PiRemoteApplication.PREF_ADDRESS, null)
                intent.data = Uri.parse(address)
                startActivity(intent)
                return true
            }
            else -> false
        }

    }

    companion object {
        val REQUEST_CODE_SETUP = 1
    }
}
