package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.utils.SingleEventLiveData
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    init {
        getPlaylistList()
    }

    private val stateLiveData = MutableLiveData<List<Playlist>>()
    private val createPlaylistsTrigger = SingleEventLiveData<Boolean>()

    fun getLiveData(): LiveData<List<Playlist>> = stateLiveData

    fun getCreatePlaylistTrigger(): LiveData<Boolean> = createPlaylistsTrigger

    fun createPlaylist() {
        createPlaylistsTrigger.postValue(true)
    }

    fun getPlaylistList() {
        viewModelScope.launch {
            interactor.getPlaylists().collect {
                stateLiveData.postValue(it)
            }
        }
    }

    fun clearPlaylists() {
        viewModelScope.launch {
            interactor.clear()
        }
    }
}