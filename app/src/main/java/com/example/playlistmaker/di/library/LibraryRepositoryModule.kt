package com.example.playlistmaker.di.library

import com.example.playlistmaker.data.library.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.utils.TrackDbConverter
import com.example.playlistmaker.domain.library.FavoriteTracksRepository
import org.koin.dsl.module

val libraryRepositoryModule = module {

    factory { TrackDbConverter() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }
}