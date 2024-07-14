package com.example.playlistmaker.domain.settings

interface SettingsRepository {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(settings: Boolean)
}