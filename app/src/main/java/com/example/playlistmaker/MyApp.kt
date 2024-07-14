package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class MyApp : Application() {
    private var nightMode = false

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
    }
}