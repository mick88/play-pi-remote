package com.michaldabski.radiopiremote

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.michaldabski.radiopiremote.api.ApiConfigurationError
import com.michaldabski.radiopiremote.api.ApiUrlBuilder
import com.michaldabski.radiopiremote.utils.BitmapCache

/**
 * Created by Michal on 30/10/2016.
 */

class PiRemoteApplication : Application() {
    private var requestQueue: RequestQueue? = null
    private var imageLoader: ImageLoader? = null

    companion object {
        val PREFERENCES_NAME = "pi-remote"
        val PREF_ADDRESS = "address"
    }

    val apiUrlBuilder: ApiUrlBuilder
        @Throws(ApiConfigurationError::class)
        get() {
            val address = sharedPreferences.getString(PREF_ADDRESS, "")
            return ApiUrlBuilder(address)
        }

    val sharedPreferences: SharedPreferences
        get() = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getRequestQueue(): RequestQueue {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this)
        }
        return requestQueue!!
    }

    fun getImageLoader(): ImageLoader {
        if (imageLoader == null) {
            imageLoader = ImageLoader(getRequestQueue(), BitmapCache())
        }
        return imageLoader!!
    }
}
