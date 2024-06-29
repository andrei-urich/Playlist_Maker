package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.model.Track

interface PlayerInteractor {
    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(track: Track, listener: OnPlayerStateChangeListener)

    interface OnPlayerStateChangeListener {
        fun onChange(state: String)
    }

    fun getCurrentPosition(): String

    fun release()
}