package com.example.playlistmaker.di.library

import com.example.playlistmaker.data.library.PlaylistRepositoryImpl
import com.example.playlistmaker.data.utils.PlaylistDbConvertor
import com.example.playlistmaker.domain.library.PlaylistRepository
import org.koin.dsl.module

val playlistRepositoryModule = module {

    factory { PlaylistDbConvertor() }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }
}