package com.example.playlistmaker.di.player

import android.media.MediaPlayer
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.OnPlayerStateChangeListener
import com.example.playlistmaker.domain.player.OnPlayerStateChangeListenerImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import org.koin.dsl.module

val playerModule = module {
    factory <PlayerRepository> {
        PlayerRepositoryImpl(get(), get())
    }

    factory <PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory {
        MediaPlayer()
    }

    factory <OnPlayerStateChangeListener> {
        OnPlayerStateChangeListenerImpl()
    }
}