package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTracksViewModel : ViewModel() {
    private var stateLiveData = MutableLiveData<Int>()

    fun getLiveData(): LiveData<Int> = stateLiveData
}