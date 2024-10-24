package com.example.playlistmaker.domain.settings

interface ExternalNavigator {
    fun shareApp()
    fun sharePlaylist(string: String)
    fun openTerms()
    fun openSupport()
}