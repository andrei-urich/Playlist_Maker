package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.use_case.SearchHistoryInteractor

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun addToHistory(track: Track) {
        repository.addToHistory(track)
    }

    override fun getHistoryList(): List<Track> {
        return repository.getHistoryList()
    }

    override fun clearHistory() {
        return repository.clearHistory()
    }

}