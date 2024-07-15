package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.Track

interface PlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(track: Track, listener: OnPlayerStateChangeListener)
    fun getCurrentPosition (): String
    fun release()
}