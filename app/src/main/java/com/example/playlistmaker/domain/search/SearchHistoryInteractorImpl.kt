package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override suspend fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun getHistoryList(): List<Track> {
        return repository.getHistoryList()
    }

    override fun clearHistory() {
        return repository.clearHistory()
    }

}