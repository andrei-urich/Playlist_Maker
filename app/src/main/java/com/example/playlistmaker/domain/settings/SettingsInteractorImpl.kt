package com.example.playlistmaker.domain.settings

class SettingsInteractorImpl(val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return settingsRepository.getThemeSettings()

    }

    override fun updateThemeSetting(settings: Boolean) {
        settingsRepository.updateThemeSetting(settings)
    }

}