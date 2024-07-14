package com.example.playlistmaker.domain.player

sealed class AudioplayerPlayState {
    object Complete : AudioplayerPlayState()
    object Prepared : AudioplayerPlayState()
    object Playing : AudioplayerPlayState()
    object Paused : AudioplayerPlayState()

}
