package com.example.playlistmaker.di.library

import com.example.playlistmaker.presentation.library.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteTracksViewModelModule = module {
    viewModel {
        FavoriteTracksViewModel()
    }
}