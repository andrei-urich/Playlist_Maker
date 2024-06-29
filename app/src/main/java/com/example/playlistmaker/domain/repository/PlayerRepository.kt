package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.use_case.PlayerInteractor

interface PlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(track: Track, listener: PlayerInteractor.OnPlayerStateChangeListener)
    fun getCurrentPosition (): String
    fun release()
}