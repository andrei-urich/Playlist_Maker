package com.example.playlistmaker.data.impl

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.NIGHT_MODE
import com.example.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(val context: Context) : SettingsRepository {
    val sharedPrefs = Creator.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES)

    override fun getThemeSettings(): Boolean {
        val nightMode = sharedPrefs.getBoolean(
            NIGHT_MODE, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.resources.configuration.isNightModeActive
            } else {
                context.resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            }
        )
        return nightMode ?: false
    }

    override fun updateThemeSetting(settings: Boolean) {
        val nightModeEnabled = settings
        AppCompatDelegate.setDefaultNightMode(
            if (nightModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit().putBoolean(NIGHT_MODE, nightModeEnabled).apply()
    }
}

