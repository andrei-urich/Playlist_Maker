package com.example.playlistmaker.di

import com.example.playlistmaker.data.utils.TrackTransferRepositoryImpl
import com.example.playlistmaker.domain.repository.TrackTransferRepository
import org.koin.dsl.module

val trackTransferRepositoryModule = module {

    factory<TrackTransferRepository> {
        TrackTransferRepositoryImpl()
    }
}