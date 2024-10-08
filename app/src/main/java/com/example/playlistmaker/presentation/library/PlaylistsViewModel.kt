package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.presentation.utils.SingleEventLiveData

class PlaylistsViewModel : ViewModel() {

    private val stateLiveData = MutableLiveData<Int>()
    private val createPlaylistsTrigger = SingleEventLiveData<Boolean>()

    fun getLiveData(): LiveData<Int> = stateLiveData
    fun getCreatePlaylistTrigger(): LiveData<Boolean> = createPlaylistsTrigger

    fun createPlaylist() {
        createPlaylistsTrigger.postValue(true)
    }
}