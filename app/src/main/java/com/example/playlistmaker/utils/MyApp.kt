package com.example.playlistmaker.utils

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.library.addPlaylistViewModelModule
import com.example.playlistmaker.di.library.dataModule
import com.example.playlistmaker.di.library.favoriteTracksInteractorModule
import com.example.playlistmaker.di.library.favoriteTracksViewModelModule
import com.example.playlistmaker.di.library.libraryRepositoryModule
import com.example.playlistmaker.di.library.libraryViewModelModule
import com.example.playlistmaker.di.library.openPlaylistViewModelModule
import com.example.playlistmaker.di.library.playlistInteractorModule
import com.example.playlistmaker.di.library.playlistRepositoryModule
import com.example.playlistmaker.di.library.playlistsViewModelModule
import com.example.playlistmaker.di.player.audioplayerViewModelModule
import com.example.playlistmaker.di.player.playerModule
import com.example.playlistmaker.di.search.searchModule
import com.example.playlistmaker.di.search.searchViewModelModule
import com.example.playlistmaker.di.settings.settingsModule
import com.example.playlistmaker.di.settings.settingsViewModelModule
import com.example.playlistmaker.di.trackTransferRepositoryModule
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(
                searchModule,
                settingsModule,
                playerModule,
                searchViewModelModule,
                settingsViewModelModule,
                audioplayerViewModelModule,
                libraryViewModelModule,
                favoriteTracksViewModelModule,
                playlistsViewModelModule,
                dataModule,
                libraryRepositoryModule,
                favoriteTracksInteractorModule,
                playlistRepositoryModule,
                playlistInteractorModule,
                addPlaylistViewModelModule,
                trackTransferRepositoryModule,
                openPlaylistViewModelModule
            )
        }

        PermissionRequester.initialize(applicationContext)

        val sharedPrefs: SharedPreferences by inject() {
            parametersOf(PLAYLIST_MAKER_PREFERENCES)
        }
        val nightModeEnabled = sharedPrefs.getBoolean(
            NIGHT_MODE, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                this.resources.configuration.isNightModeActive
            } else {
                this.resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            }
        )
        AppCompatDelegate.setDefaultNightMode(
            if (nightModeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}