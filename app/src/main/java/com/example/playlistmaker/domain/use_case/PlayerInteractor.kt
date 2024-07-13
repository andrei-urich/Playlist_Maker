package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.OnPlayerStateChangeListener
import com.example.playlistmaker.domain.model.Track

interface PlayerInteractor {

    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(track: Track, listener: OnPlayerStateChangeListener)

    fun getCurrentPosition(): String

    fun release()
}