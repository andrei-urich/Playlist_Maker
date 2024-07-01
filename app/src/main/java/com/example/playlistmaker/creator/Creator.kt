package com.example.playlistmaker.creator

import com.example.playlistmaker.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.impl.TrackTransferRepositoryImpl
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TrackSearchUseCaseImpl
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.use_case.PlayerInteractor
import com.example.playlistmaker.domain.use_case.TrackSearchUseCase
import com.example.playlistmaker.ui.repository.TrackTransferRepository

object Creator {
    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksSearch(): TrackSearchUseCase {
        return TrackSearchUseCaseImpl(getTrackRepository())
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
