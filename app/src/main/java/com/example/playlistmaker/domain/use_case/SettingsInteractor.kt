package com.example.playlistmaker.domain.use_case


interface SettingsInteractor {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(settings: Boolean)
}