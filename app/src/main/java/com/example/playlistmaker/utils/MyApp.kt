package com.example.playlistmaker.utils

import android.app.Application
import android.content.Context
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.search.searchModule
import com.example.playlistmaker.di.search.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MyApp)
            // Load modules
            modules(searchModule, viewModelModule)
        }
    }
}