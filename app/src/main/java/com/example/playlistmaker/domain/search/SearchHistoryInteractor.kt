package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {

    suspend fun addToHistory(track: Track)
    fun getHistoryList(): Flow<List<Track>>
    fun clearHistory()
}