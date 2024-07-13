package com.example.playlistmaker.domain.model

sealed class AudioplayerPlayState {
    object Complete : AudioplayerPlayState()
    object Prepared : AudioplayerPlayState()
    object Playing : AudioplayerPlayState()
    object Paused : AudioplayerPlayState()

}
