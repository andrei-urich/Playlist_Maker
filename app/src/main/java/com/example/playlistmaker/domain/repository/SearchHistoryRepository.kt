package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    fun addToHistory(track: Track)
    fun getHistoryList(): List<Track>
    fun clearHistory()
}