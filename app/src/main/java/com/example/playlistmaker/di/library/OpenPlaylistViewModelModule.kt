package com.example.playlistmaker.di.library

import com.example.playlistmaker.presentation.library.OpenPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val openPlaylistViewModelModule = module {
    viewModel {
        OpenPlaylistViewModel(get(), get())
    }
}