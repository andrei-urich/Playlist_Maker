package com.example.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.FavoriteTracksInteractor
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.player.OnPlayerStateChangeListener
import com.example.playlistmaker.domain.player.AudioplayerPlayState
import com.example.playlistmaker.domain.player.PlayerState.STATE_COMPLETE
import com.example.playlistmaker.domain.player.PlayerState.STATE_PAUSED
import com.example.playlistmaker.domain.player.PlayerState.STATE_PLAYING
import com.example.playlistmaker.domain.player.PlayerState.STATE_PREPARED
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.presentation.utils.SingleEventLiveData
import kotlinx.coroutines.launch


class AudioplayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val playStatusLiveData = MutableLiveData<AudioplayerPlayState>()
    private var favoriteStateLiveData = MutableLiveData<Boolean>()
    private var playlistLiveData = MutableLiveData<List<Playlist>>()
    private var trackToPlaylistLiveData = SingleEventLiveData<Pair<Boolean, String>>()

    fun getPlayStatusLiveData(): LiveData<AudioplayerPlayState> = playStatusLiveData
    fun getFavoriteStateLiveData(): LiveData<Boolean> = favoriteStateLiveData
    fun getPlaylistLiveData(): LiveData<List<Playlist>> = playlistLiveData
    fun getTrackToPlaylistLiveData(): LiveData<Pair<Boolean, String>> = trackToPlaylistLiveData

    init {
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

    fun onFavoriteClicked(track: Track) {
        if (track.isFavorite) {
            track.isFavorite = false
            favoriteStateLiveData.postValue(false)
            viewModelScope.launch {
                favoriteTracksInteractor.deleteTrackById(track.trackId)
            }
        } else {
            track.isFavorite = true
            favoriteStateLiveData.postValue(true)
            viewModelScope.launch {
                favoriteTracksInteractor.addTrackToFavorite(track)
            }
        }
    }

    fun getPlaylistsList() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                playlistLiveData.postValue(it)
            }
        }
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        val trackIdList = playlistInteractor.getTrackIdListAsInt(playlist)
        if (track.trackId in trackIdList) {
            trackToPlaylistLiveData.postValue(Pair(false, playlist.name))
        } else {
            trackIdList.add(track.trackId)
            playlist.trackIds = playlistInteractor.getTrackIdListAsString(trackIdList)
            playlist.tracksCount = trackIdList.size
            viewModelScope.launch {
                playlistInteractor.updatePlaylist(playlist)
                playlistInteractor.addTrackToPlaylist(track, playlist)
            }
            trackToPlaylistLiveData.postValue(Pair(true, playlist.name))
        }

    }

    override fun onCleared() {
        playerInteractor.release()
    }
}

