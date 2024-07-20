package com.example.playlistmaker.di.search


import android.content.Context
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.PlaylistAPI
import com.example.playlistmaker.data.search.RetrofitNetworkClient
import com.example.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.search.TracksSearchRepositoryImpl
import com.example.playlistmaker.data.utils.TrackTransferRepositoryImpl
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.search.TrackSearchInteractor
import com.example.playlistmaker.domain.search.TrackSearchInteractorImpl
import com.example.playlistmaker.domain.search.TracksSearchRepository
import com.example.playlistmaker.ui.repository.TrackTransferRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {

    single<TracksSearchRepository> {
        TracksSearchRepositoryImpl(get())
    }
    single<PlaylistAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaylistAPI::class.java)
    }

    factory { Gson() }

    single<TrackSearchInteractor> {
        TrackSearchInteractorImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    single <TrackTransferRepository> {
        TrackTransferRepositoryImpl ()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single {
        androidContext()
            .getSharedPreferences("search_history", Context.MODE_PRIVATE)
    }
}