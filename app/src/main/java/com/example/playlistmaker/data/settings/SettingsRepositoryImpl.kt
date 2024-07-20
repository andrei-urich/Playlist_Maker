package com.example.playlistmaker.data.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.utils.NIGHT_MODE
import com.example.playlistmaker.domain.settings.SettingsRepository

class SettingsRepositoryImpl(
    val context: Context,
    val sharedPrefs: SharedPreferences

) : SettingsRepository {

    override fun getThemeSettings(): Boolean {
        val nightMode = sharedPrefs.getBoolean(
            NIGHT_MODE, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.resources.configuration.isNightModeActive
            } else {
                context.resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            }
        )
        return nightMode
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

