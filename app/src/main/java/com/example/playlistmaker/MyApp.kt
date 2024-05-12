package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class MyApp : Application() {
    private var nightMode = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        nightMode = sharedPrefs.getBoolean(
            NIGHT_MODE, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                resources.configuration.isNightModeActive
            } else {
                resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            }
        )
        switchTheme(nightMode)
    }

    fun switchTheme(nightModeEnabled: Boolean) {
        nightMode = nightModeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (nightModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}