package com.example.playlistmaker.presentation.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.settings.SharingInteractor
import com.example.playlistmaker.presentation.utils.SingleEventLiveData
import com.example.playlistmaker.utils.Formatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OpenPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private var isClickAllowed = true

    private var playTrackTrigger = SingleEventLiveData<Track>()
    fun getPlayTrackTrigger(): LiveData<Track> = playTrackTrigger
    private val trackListLiveData = MutableLiveData<List<Track>>()
    fun getTrackListLiveData(): LiveData<List<Track>> = trackListLiveData
    private val shareIntentLiveData = SingleEventLiveData<Boolean>()
    fun getShareIntentLiveData(): LiveData<Boolean> = shareIntentLiveData

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

    fun sharePlaylist(playlist: Playlist, trackList: List<Track>) {
        if (playlist.tracksCount == 0 || trackList.isNullOrEmpty()) {
            shareIntentLiveData.postValue(false)
        } else {
            val title = playlist.name + "\n"
            val description = playlist.description + "\n"
            val tracksCount =
                playlist.tracksCount.toString() + " " + Formatter.formatTracks(playlist.tracksCount) + "\n"
            val stringBuilder = StringBuilder()
            stringBuilder.append(title, description, tracksCount)
            for (track in trackList) {
                val string =
                    (trackList.indexOf(track) + 1).toString() + "." + " " + track.artistName + " " + "-" + " " + track.trackName + " " + "(" + track.trackTime + ")" + "\n"
                stringBuilder.append(string)
            }
            val message = stringBuilder.toString()
            Log.d("MY", message)
            sharingInteractor.sharePlaylist(message)
        }
    }

    fun delete(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
