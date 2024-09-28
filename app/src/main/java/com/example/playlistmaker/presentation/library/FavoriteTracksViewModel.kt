package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.FavoriteTracksInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.utils.SingleEventLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<List<Track>>()
    private var favoriteTracksList = emptyList<Track>()
    private var playTrackTrigger = SingleEventLiveData<Track>()
    private var isClickAllowed = true
    fun getLiveData(): LiveData<List<Track>> = stateLiveData
    fun getPlayTrackTrigger(): LiveData<Track> = playTrackTrigger

   init {
        viewModelScope.launch {

//            favoriteTracksInteractor.getFavoriteTracksList().collect {
//                favoriteTracksList
//            }
            var list = favoriteTracksInteractor.getFavoriteTracksList()
            stateLiveData.postValue(list)
        }
    }

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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}

