package com.example.playlistmaker.di.library

import com.example.playlistmaker.presentation.library.AddPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addPlaylistViewModelModule = module {
    viewModel {
        AddPlaylistViewModel(get())
    }
}