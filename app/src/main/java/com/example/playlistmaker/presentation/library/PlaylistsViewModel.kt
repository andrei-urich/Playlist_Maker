package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.utils.SingleEventLiveData

class PlaylistsViewModel : ViewModel() {

    private val stateLiveData = MutableLiveData<List<Playlist>>()
    private val createPlaylistsTrigger = SingleEventLiveData<Boolean>()

    fun getLiveData(): LiveData<List<Playlist>> = stateLiveData

    fun getCreatePlaylistTrigger(): LiveData<Boolean> = createPlaylistsTrigger

    fun createPlaylist() {
        createPlaylistsTrigger.postValue(true)
    }
}