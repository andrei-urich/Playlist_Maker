package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.TrackTransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.math.log


class SearchHistoryRepositoryImpl(
    private val trackTransfer: TrackTransferRepository,
) : SearchHistoryRepository, KoinComponent {
    companion object {
        const val SEARCH_HISTORY = "search_history"
    }

    private val sharedPrefs: SharedPreferences by inject() {
        parametersOf(SEARCH_HISTORY)
    }
    private var historyList = sharedPrefs.getString(SEARCH_HISTORY, null)
    private var searchHistoryTracks: MutableList<Track>
    private lateinit var currentHistoryList: MutableList<Track>

    init {
        if (!historyList.isNullOrBlank())
        { Log.d("my", "$historyList")
            searchHistoryTracks = trackTransfer.getTrackList(historyList!!)}
        else searchHistoryTracks =
            emptyList<Track>().toMutableList()
    }

    override suspend fun addToHistory(track: Track) {
        currentHistoryList = checkDuplicates(track, searchHistoryTracks)
        currentHistoryList.add(0, track)
        if (currentHistoryList.size > 10) currentHistoryList.removeLast()
        historyList = trackTransfer.sendTrackList(currentHistoryList)
        sharedPrefs.edit().putString(SEARCH_HISTORY, historyList).apply()
    }


    override fun getHistoryList(): Flow<List<Track>> = flow {
        historyList = sharedPrefs.getString(SEARCH_HISTORY, null)
        Log.d("my", "historyList = $historyList")
        emit(trackTransfer.getTrackList(historyList.toString()))
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