package com.example.playlistmaker.presentation.state

import com.example.playlistmaker.domain.model.Track

sealed class SearchHistoryState {
    object HideHistory : SearchHistoryState()
    object ClearHistory : SearchHistoryState()
    data class ShowHistory(
        val trackList: List<Track>,
    ) : SearchHistoryState()
}
