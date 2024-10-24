package com.example.playlistmaker.domain.settings

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareApp()
    }

    override fun sharePlaylist(string: String) {
        externalNavigator.sharePlaylist(string)
    }

    override fun openTerms() {
        externalNavigator.openTerms()
    }

    override fun openSupport() {
        externalNavigator.openSupport()
    }
}