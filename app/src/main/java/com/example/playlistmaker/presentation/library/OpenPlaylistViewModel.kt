package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.utils.SingleEventLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OpenPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var isClickAllowed = true

    private var playTrackTrigger = SingleEventLiveData<Track>()
    fun getPlayTrackTrigger(): LiveData<Track> = playTrackTrigger
    private val trackListLiveData = MutableLiveData<List<Track>>()
    fun getTrackListLiveData(): LiveData<List<Track>> = trackListLiveData


    fun playTrack(track: Track) {
        if (clickDebounce()) {
            playTrackTrigger.postValue(track)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun getTrackList(playlist: Playlist) {
        val listIds = playlistInteractor.getTrackIdListAsInt(playlist)
        viewModelScope.launch {
            playlistInteractor.getTrackById(listIds).collect { it ->
                trackListLiveData.postValue(it)
            }
        }
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
