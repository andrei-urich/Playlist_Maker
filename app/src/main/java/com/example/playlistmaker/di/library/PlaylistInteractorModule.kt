package com.example.playlistmaker.di.library

import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.library.PlaylistInteractorImpl
import org.koin.dsl.module

val playlistInteractorModule = module {
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}