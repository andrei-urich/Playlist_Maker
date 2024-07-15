package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.Track

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
) : PlayerInteractor {

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun preparePlayer(
        track: Track,
        listener: OnPlayerStateChangeListener
    ) {
        playerRepository.preparePlayer(track, listener)
    }

    override fun getCurrentPosition(): String {
        return playerRepository.getCurrentPosition()
    }

    override fun release() {
        playerRepository.release()
    }

}