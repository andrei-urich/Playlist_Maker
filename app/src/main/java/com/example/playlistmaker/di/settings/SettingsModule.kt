package com.example.playlistmaker.di.settings

import com.example.playlistmaker.data.settings.ExternalNavigatorImpl
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.settings.ExternalNavigator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.SharingInteractor
import com.example.playlistmaker.domain.settings.SharingInteractorImpl
import org.koin.dsl.module

val settingsModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single <ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}