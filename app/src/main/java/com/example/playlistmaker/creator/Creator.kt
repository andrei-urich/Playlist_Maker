package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.impl.TrackTransferRepositoryImpl
import com.example.playlistmaker.data.impl.SearchTracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TrackSearchInteractorImpl
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.repository.SearchTracksRepository
import com.example.playlistmaker.domain.use_case.PlayerInteractor
import com.example.playlistmaker.domain.use_case.TrackSearchInteractor
import com.example.playlistmaker.ui.repository.TrackTransferRepository

object Creator : Application() {
    private fun getTrackRepository(): SearchTracksRepository {
        return SearchTracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackRepository())
    }


    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideTrackTransfer(): TrackTransferRepository {
        return TrackTransferRepositoryImpl()
    }
}
