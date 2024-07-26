package com.example.playlistmaker.di.settings

import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsViewModelModule = module {
    viewModel {
        SettingsViewModel(get(), get())
    }
}