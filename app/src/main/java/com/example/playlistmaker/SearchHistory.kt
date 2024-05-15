package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val sharedPrefs: SharedPreferences) {
    var historyList = sharedPrefs.getString(SEARCH_HISTORY, null)
    lateinit var searchHistoryTracks: MutableList<Track>

    init {
        searchHistoryTracks = getTrackFromHistory()
    }

    fun saveTrackToHistory(track: Track) {
        var currentHistoryList = getTrackFromHistory()
//      currentHistoryList=checkDuplicates(track, currentHistoryList)
        currentHistoryList.add(0, track)
        if (currentHistoryList.size > 10) currentHistoryList.removeLast()
        historyList = Gson().toJson(currentHistoryList)
        sharedPrefs.edit().clear().apply()
    }

    fun getTrackFromHistory(): MutableList<Track> {
        val typeToken = object : TypeToken<MutableList<Track>>() {}.type
        if (!historyList.isNullOrBlank()) {
            return Gson().fromJson<MutableList<Track>>(historyList, typeToken).toMutableList()
        }
        return emptyList<Track>().toMutableList()
    }

    fun clearHistory() {
        searchHistoryTracks.clear()
        sharedPrefs.edit().putString(SEARCH_HISTORY, null).apply()
    }

    fun checkDuplicates(track: Track, list: MutableList<Track>): MutableList<Track> {
        for (t in list) {
            if (track.trackId == t.trackId) list.remove(t)
        }
        return list
    }
}