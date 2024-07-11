package com.example.playlistmaker

import com.example.playlistmaker.domain.model.Track

sealed class TrackSearchState {
    object Loading : TrackSearchState()
    object Error : TrackSearchState()
    data class Content(
        val trackList: List<Track>,
    ) : TrackSearchState()
}

