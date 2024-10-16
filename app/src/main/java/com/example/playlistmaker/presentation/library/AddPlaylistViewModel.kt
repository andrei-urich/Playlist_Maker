package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.utils.EMPTY_STRING
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch

class AddPlaylistViewModel(
    private val interactor: PlaylistInteractor
) : ViewModel() {

    private var name = EMPTY_STRING
    private var description = EMPTY_STRING
    private var cover = EMPTY_STRING
    lateinit var playlist: Playlist

    private val requester = PermissionRequester.instance()

    private val coverLiveData = MutableLiveData<String>()
    private val toggleButtonLiveData = MutableLiveData<Boolean>()
    private val stateLiveData = MutableLiveData<String>()
    private val permissionLiveData = MutableLiveData<PermissionResult>()

    fun getToggleButtonLiveData(): LiveData<Boolean> = toggleButtonLiveData
    fun getStateLiveData(): LiveData<String> = stateLiveData
    fun getCoverLiveData(): LiveData<String> = coverLiveData
    fun getPermissionLiveData(): LiveData<PermissionResult> = permissionLiveData

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

    fun addPlaylist(track: Track?) {
        if (name.isNotBlank()) {
            var trackId = EMPTY_STRING
            var trackCount = 0
            var isTrackAdded = false
            if (track != null) {
                isTrackAdded = true
                trackId = track.trackId.toString()
                trackCount = 1
            }
            playlist = Playlist(0, name, description, cover, trackId, trackCount)
            viewModelScope.launch {
                interactor.addPlaylist(playlist)
                if (cover.isNotBlank()) {
                    tryToSaveCover(playlist)
                }
                if (track != null) {
                    interactor.addTrackToPlaylist(track, playlist)
                }
            }
            exit(isTrackAdded)
        }
    }

    fun tryToSaveCover(playlist: Playlist) {
        viewModelScope.launch {
            requester.request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {
                            interactor.saveImageToExternalStorage(playlist)
                        }

                        else -> {
                            permissionLiveData.postValue(result)
                        }
                    }
                }
        }
    }

    fun exitOrStay() {
        if (name.isNotBlank() || description.isNotBlank() || cover.isNotBlank()) {
            stateLiveData.postValue(EXIT_OR_STAY)
        } else exit(false)
    }

    private fun exit(flag: Boolean) {
        if (flag) stateLiveData.postValue(EXIT_TRACK_ADDED) else stateLiveData.postValue(EXIT)

    }

    fun setEditedName(editedName: String) {
        name = editedName.trim()
        toggleButtonLiveData.postValue(name.isNotBlank())
    }

    companion object {
        const val EXIT_OR_STAY = "EXIT_OR_STAY"
        const val EXIT = "EXIT"
        const val EXIT_TRACK_ADDED = "EXIT_TRACK_ADDED"
    }

}