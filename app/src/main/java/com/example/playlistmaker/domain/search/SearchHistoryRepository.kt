package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun addToHistory(track: Track)
    fun getHistoryList(): MutableList<Track>
    fun clearHistory()
}