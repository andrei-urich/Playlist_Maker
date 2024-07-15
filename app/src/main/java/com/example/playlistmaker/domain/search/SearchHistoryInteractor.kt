package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {

    fun addToHistory(track: Track)
    fun getHistoryList(): List<Track>
    fun clearHistory()
}