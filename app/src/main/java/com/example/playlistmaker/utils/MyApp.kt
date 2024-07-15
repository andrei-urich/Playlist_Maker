package com.example.playlistmaker.utils

import android.app.Application
import com.example.playlistmaker.creator.Creator

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
    }
}