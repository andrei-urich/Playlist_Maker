package com.example.playlistmaker.utils

import android.app.Application
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.search.searchModule
import com.example.playlistmaker.di.search.searchViewModelModule
import com.example.playlistmaker.di.settings.settingsModule
import com.example.playlistmaker.di.settings.settingsViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(searchModule, settingsModule, searchViewModelModule, settingsViewModelModule)
        }
    }
}