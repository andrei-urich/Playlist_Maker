package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel : ViewModel() {
    private val stateLiveData = MutableLiveData<Int>()

    fun getLiveData(): LiveData<Int> = stateLiveData
}