package com.example.playlistmaker.creator

import com.example.playlistmaker.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TrackSearchImpl
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.use_case.PlayerInteractor
import com.example.playlistmaker.domain.use_case.TrackSearch

object Creator {
    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksSearch(): TrackSearch {
        return TrackSearchImpl(getTrackRepository())
    }


    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}
