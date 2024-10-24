package com.example.playlistmaker.domain.settings

interface SharingInteractor {
    fun shareApp()
    fun sharePlaylist(string: String)
    fun openTerms()
    fun openSupport()
}