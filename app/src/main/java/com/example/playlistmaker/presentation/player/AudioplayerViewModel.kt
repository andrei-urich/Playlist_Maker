package com.example.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.OnPlayerStateChangeListener
import com.example.playlistmaker.domain.player.AudioplayerPlayState
import com.example.playlistmaker.domain.player.PlayerState.STATE_COMPLETE
import com.example.playlistmaker.domain.player.PlayerState.STATE_PAUSED
import com.example.playlistmaker.domain.player.PlayerState.STATE_PLAYING
import com.example.playlistmaker.domain.player.PlayerState.STATE_PREPARED
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerInteractor

class AudioplayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val playStatusLiveData = MutableLiveData<AudioplayerPlayState>()

    fun getPlayStatusLiveData(): LiveData<AudioplayerPlayState> = playStatusLiveData

    fun initPlayer() {
        preparePlayer()
    }

    fun getAction() {
        if (playStatusLiveData.value is AudioplayerPlayState.Playing) pause() else play()
    }

    fun play() {
        playerInteractor.startPlayer()
        runState(STATE_PLAYING)
    }

    fun preparePlayer() {
        track.let {
            playerInteractor.preparePlayer(it, object : OnPlayerStateChangeListener {
                override fun onChange(state: String) {
                    runState(state)
                }
            })
        }
    }

    fun pause() {
        playerInteractor.pausePlayer()
        runState(STATE_PAUSED)
    }

    private fun runState(state: String) {
        when (state) {
            STATE_PREPARED -> {
                playStatusLiveData.postValue(AudioplayerPlayState.Prepared)
            }

            STATE_PLAYING -> {
                playStatusLiveData.postValue(AudioplayerPlayState.Playing)
            }

            STATE_PAUSED -> {
                playStatusLiveData.postValue(AudioplayerPlayState.Paused)
            }

            STATE_COMPLETE -> {
                playStatusLiveData.postValue(AudioplayerPlayState.Complete)
            }
        }
    }

    fun getCurrentPosition(): String {
        return playerInteractor.getCurrentPosition()

    }

    override fun onCleared() {
        playerInteractor.release()
    }
}

