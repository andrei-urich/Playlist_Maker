package com.example.playlistmaker.presentation.search

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.ConsumerData
import com.example.playlistmaker.domain.search.SearchConsumer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TrackSearchInteractor
import com.example.playlistmaker.presentation.utils.SingleEventLiveData
import com.example.playlistmaker.utils.EMPTY_STRING

class SearchViewModel(
    private val trackSearchInteractor: TrackSearchInteractor = Creator.provideTrackSearchInteractor(),
    private val searchHistoryInteractor: SearchHistoryInteractor = Creator.provideSearchHistoryInteractor()
) : ViewModel() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    var searchText = EMPTY_STRING
    private val searchRunnable = Runnable { request(searchText) }

    private var searchStateLiveData = MutableLiveData<TrackSearchState>()
    private var playTrackTrigger = SingleEventLiveData<Track>()

    fun getSearchStateLiveData(): LiveData<TrackSearchState> = searchStateLiveData
    fun getPlayTrackTrigger(): LiveData<Track> = playTrackTrigger


    fun getSearchText(searchText: String) {
        this.searchText= searchText
        searchDebounce()
    }

    fun request(request: String) {
        if (request.isNotEmpty()) {
            searchStateLiveData.postValue(TrackSearchState.Loading)
            trackSearchInteractor.search(
                request,
                consumer = object : SearchConsumer<List<Track>> {
                    override fun consume(data: ConsumerData<List<Track>>) {
                        when (data) {
                            is ConsumerData.Error -> searchStateLiveData.postValue(
                                TrackSearchState.Error
                            )

                            is ConsumerData.Data -> {
                                val tracks: List<Track> = data.value
                                searchStateLiveData.postValue(TrackSearchState.Content(tracks))
                            }
                        }
                    }
                }
            )
        }
    }

    fun playTrack(track: Track) {
        if (clickDebounce()) {
            searchHistoryInteractor.addToHistory(track)
            playTrackTrigger.postValue(track)
        }
    }

    fun playTrackFromHistory(track: Track) {
        playTrackTrigger.postValue(track)
    }

    fun getHistoryList(): List<Track> {
        return searchHistoryInteractor.getHistoryList()
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    // метод дебаунса клика на результатах поиска (клик на треке для вызова плеера)
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    // метод дебаунса запуска поиска
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onCleared() {
        if (searchRunnable != null) {
            handler.removeCallbacks(searchRunnable)
        }
        super.onCleared()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel()
                }
            }
        }
    }
}



