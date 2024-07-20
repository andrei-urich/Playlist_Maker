package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.settings.ExternalNavigatorImpl
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.data.utils.TrackTransferRepositoryImpl
import com.example.playlistmaker.data.search.TracksSearchRepositoryImpl
import com.example.playlistmaker.data.search.RetrofitNetworkClient
import com.example.playlistmaker.domain.player.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.SharingInteractorImpl
import com.example.playlistmaker.domain.search.TrackSearchInteractorImpl
import com.example.playlistmaker.domain.settings.ExternalNavigator
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.search.TracksSearchRepository
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SharingInteractor
import com.example.playlistmaker.domain.search.TrackSearchInteractor
import com.example.playlistmaker.ui.repository.TrackTransferRepository

object Creator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

//    private fun getTrackSearchRepository(): TracksSearchRepository {
//        return TracksSearchRepositoryImpl(RetrofitNetworkClient())
//    }

//    fun provideTrackSearchInteractor(): TrackSearchInteractor {
//        return TrackSearchInteractorImpl(getTrackSearchRepository())
//    }


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

//    fun getSearchHistoryRepository(): SearchHistoryRepository {
//        return SearchHistoryRepositoryImpl()
//    }
//
//    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
//        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
//    }

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
