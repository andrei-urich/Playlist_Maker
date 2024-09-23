package com.example.playlistmaker.di.library

import com.example.playlistmaker.domain.library.FavoriteTracksInteractor
import com.example.playlistmaker.domain.library.FavoriteTracksInteractorImpl
import org.koin.dsl.module

val favoriteTracksInteractorModule = module {
    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }
}