package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

class MyApp : Application() {
    private var nightMode = false

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
    }
}