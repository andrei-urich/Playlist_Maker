package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.use_case.SettingsInteractor
import com.example.playlistmaker.domain.use_case.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor = Creator.provideSharingInteractor(),
    private val settingsInteractor: SettingsInteractor = Creator.provideSettingsInteractor()
) : ViewModel() {

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun getThemeSettings() {
        settingsInteractor.getThemeSettings()
    }

    fun updateThemeSetting(settings: Boolean) {
        settingsInteractor.updateThemeSetting(settings)
    }
}