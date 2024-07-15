package com.example.playlistmaker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SharingInteractor

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

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SettingsViewModel()
                }
            }
        }
    }
}