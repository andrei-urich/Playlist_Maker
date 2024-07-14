package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.impl.TrackTransferRepositoryImpl
import com.example.playlistmaker.data.impl.TracksSearchRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.domain.impl.TrackSearchInteractorImpl
import com.example.playlistmaker.domain.repository.ExternalNavigator
import com.example.playlistmaker.domain.repository.PlayerRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.repository.TracksSearchRepository
import com.example.playlistmaker.domain.use_case.PlayerInteractor
import com.example.playlistmaker.domain.use_case.SearchHistoryInteractor
import com.example.playlistmaker.domain.use_case.SettingsInteractor
import com.example.playlistmaker.domain.use_case.SharingInteractor
import com.example.playlistmaker.domain.use_case.TrackSearchInteractor
import com.example.playlistmaker.ui.repository.TrackTransferRepository

object Creator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getTrackSearchRepository(): TracksSearchRepository {
        return TracksSearchRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTrackSearchRepository())
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

    fun getSharedPreferences(name: String): SharedPreferences {
        return application.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl()
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }
    fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }


}
