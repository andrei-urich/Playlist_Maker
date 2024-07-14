package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.repository.ExternalNavigator
import com.example.playlistmaker.domain.use_case.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareApp()
    }

    override fun openTerms() {
        externalNavigator.openTerms()
    }

    override fun openSupport() {
        externalNavigator.openSupport()
    }
}