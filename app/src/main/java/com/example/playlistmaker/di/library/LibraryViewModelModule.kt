package com.example.playlistmaker.di.library

import com.example.playlistmaker.presentation.library.LibraryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryViewModelModule= module {
    viewModel {
        LibraryViewModel()
    }
}
