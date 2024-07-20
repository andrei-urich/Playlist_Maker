package com.example.playlistmaker.di.search

import com.example.playlistmaker.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }
}