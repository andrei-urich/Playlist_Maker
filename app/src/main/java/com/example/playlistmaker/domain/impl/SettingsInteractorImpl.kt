package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.use_case.SettingsInteractor

class SettingsInteractorImpl(val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return settingsRepository.getThemeSettings()

    }

    override fun updateThemeSetting(settings: Boolean) {
        settingsRepository.updateThemeSetting(settings)
    }

}