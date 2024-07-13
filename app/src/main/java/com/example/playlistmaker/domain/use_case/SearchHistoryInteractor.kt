package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {

    fun addToHistory(track: Track)
    fun getHistoryList(): List<Track>
    fun clearHistory()
}