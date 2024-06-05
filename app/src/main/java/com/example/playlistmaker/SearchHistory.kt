package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val sharedPrefs: SharedPreferences) {
    companion object {
        const val SEARCH_HISTORY = "search_history"
    }

    var historyList = sharedPrefs.getString(SEARCH_HISTORY, null)
    var searchHistoryTracks: MutableList<Track>

    init {
        searchHistoryTracks = getTrackFromHistory()
    }

    fun saveTrackToHistory(track: Track) {
        var currentHistoryList = getTrackFromHistory()
        currentHistoryList = checkDuplicates(track, currentHistoryList)
        currentHistoryList.add(0, track)
        if (currentHistoryList.size > 10) currentHistoryList.removeLast()
        historyList = Gson().toJson(currentHistoryList)
        sharedPrefs.edit().putString(SEARCH_HISTORY, historyList).apply()
    }

    fun getTrackFromHistory(): MutableList<Track> {
        val typeToken = object : TypeToken<MutableList<Track>>() {}.type
        historyList = sharedPrefs.getString(SEARCH_HISTORY, null)
        if (!historyList.isNullOrBlank()) {
            return Gson().fromJson<MutableList<Track>>(historyList, typeToken).toMutableList()
        }
        return emptyList<Track>().toMutableList()
    }

    fun clearHistory() {
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