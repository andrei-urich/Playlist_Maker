package com.example.playlistmaker.creator

import com.example.playlistmaker.data.dto.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.TrackSearchImpl
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.use_case.TrackSearch

object Creator {
    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksSearch(): TrackSearch {
        return TrackSearchImpl(getTrackRepository())
    }
}