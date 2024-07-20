package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.ui.repository.TrackTransferRepository

class SearchHistoryRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val trackTransfer: TrackTransferRepository
) : SearchHistoryRepository {
    companion object {
        const val SEARCH_HISTORY = "search_history"
    }

    private var historyList = sharedPrefs.getString(SEARCH_HISTORY, null)
    private var searchHistoryTracks: MutableList<Track>

    init {
        searchHistoryTracks = getHistoryList()
    }

    override fun addToHistory(track: Track) {
        var currentHistoryList = this.getHistoryList()
        currentHistoryList = checkDuplicates(track, currentHistoryList)
        currentHistoryList.add(0, track)
        if (currentHistoryList.size > 10) currentHistoryList.removeLast()
        historyList = trackTransfer.sendTrackList(currentHistoryList)
        sharedPrefs.edit().putString(SEARCH_HISTORY, historyList).apply()
    }


    override fun getHistoryList(): MutableList<Track> {
        historyList = sharedPrefs.getString(SEARCH_HISTORY, null)
        if (!historyList.isNullOrBlank()) {
            return trackTransfer.getTrackList(historyList!!)
        }
        return emptyList<Track>().toMutableList()
    }

    override fun clearHistory() {
        sharedPrefs.edit().clear().apply()
    }

    private fun checkDuplicates(track: Track, list: MutableList<Track>): MutableList<Track> {
        var index = -1
        val trackIDs = list.map { it.trackId }
        for (i in 1..trackIDs.size) {
            if (track.trackId == trackIDs[i - 1]) index = i - 1
        }
        if (index >= 0) {
            list.removeAt(index)
        }
        return list
    }
}