package com.example.playlistmaker.di.library

import com.example.playlistmaker.presentation.library.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PlaylistsViewModelModule = module {
    viewModel {
        PlaylistsViewModel()
    }
}