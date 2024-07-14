package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track

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