package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.utils.EMPTY_STRING
import kotlinx.coroutines.launch

class AddPlaylistViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    private var name = EMPTY_STRING
    private var description = EMPTY_STRING
    private var cover = EMPTY_STRING

    private val coverLiveData = MutableLiveData<String>()
    private val toggleButtonLiveData = MutableLiveData<Boolean>()
    private val stateLiveData = MutableLiveData<String>()
    fun getToggleButtonLiveData(): LiveData<Boolean> = toggleButtonLiveData
    fun getStateLiveData(): LiveData<String> = stateLiveData
    fun getCoverLiveData(): LiveData<String> = coverLiveData

    fun setName(string: String) {
        name = string.trim()
        toggleButtonLiveData.postValue(name.isNotBlank())
    }

    fun setDescription(string: String) {
        description = string.trim()
    }

    fun setCoverImage(string: String) {
        cover = string
        coverLiveData.postValue(cover)
    }

    fun addPlaylist() {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                val playlist = Playlist(name, description, cover, EMPTY_STRING, 0)
                interactor.addPlaylist(playlist)
                if (cover.isNotBlank()) {
                    interactor.saveImageToExternalStorage(playlist)
                }
            }
        }
    }


    fun goOrStay() {
        stateLiveData.postValue(GO_OR_STAY)
    }

    fun go() {
        stateLiveData.postValue(GO)
    }

    fun stay() {
        stateLiveData.postValue(STAY)
    }


    companion object {
        const val GO_OR_STAY = "GO_OR_STAY"
        const val GO = "GO"
        const val STAY = "STAY"
        const val ADD_OK = "ADD_OK"
    }

}