package com.example.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.FavoriteTracksInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TrackSearchInteractor
import com.example.playlistmaker.presentation.utils.SingleEventLiveData
import com.example.playlistmaker.utils.EMPTY_STRING
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val trackSearchInteractor: TrackSearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {

    private var isClickAllowed = true
    var searchText = EMPTY_STRING
    private var searchJob: Job? = null

    private var searchStateLiveData = MutableLiveData<TrackSearchState>()
    private var searchHistoryStateLiveData = MutableLiveData<Boolean>()
    private var searchHistoryListData = MutableLiveData<List<Track>>()
    private var playTrackTrigger = SingleEventLiveData<Track>()


    fun getSearchStateLiveData(): LiveData<TrackSearchState> = searchStateLiveData
    fun getPlayTrackTrigger(): LiveData<Track> = playTrackTrigger
    fun getSearchHistoryState(): LiveData<Boolean> = searchHistoryStateLiveData
    fun getSearchHistoryListData(): LiveData<List<Track>> = searchHistoryListData

    fun getSearchText(searchText: String) {
        this.searchText = searchText
        searchDebounce(searchText)
    }

    fun request(request: String) {
        if (request.isNotEmpty()) {
            searchStateLiveData.postValue(TrackSearchState.Loading)

            viewModelScope.launch {
                trackSearchInteractor.search(
                    request
                ).collect { pair ->
                    when (pair.first) {
                        null -> searchStateLiveData.postValue(
                            TrackSearchState.Error
                        )

                        else -> {
                            val tracks: List<Track> = pair.first as List<Track>
                            searchStateLiveData.postValue(TrackSearchState.Content(tracks))
                        }
                    }
                }
            }
        }
    }


    fun playTrack(track: Track) {
        if (clickDebounce()) {
            viewModelScope.launch {
                searchHistoryInteractor.addToHistory(track)
                playTrackTrigger.postValue(track)
            }
        }
    }

    fun playTrackFromHistory(track: Track) {
        playTrackTrigger.postValue(track)
    }

    fun getHistoryList() {
        viewModelScope.launch {
            searchHistoryInteractor.getHistoryList().collect {
                searchHistoryListData.postValue(it)
            }
        }
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    // метод дебаунса клика на результатах поиска (клик на треке для вызова плеера)
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun searchDebounce(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            request(searchText)
        }
    }

    fun setSearchHistoryState(state: Boolean) {
        when (state) {
            true -> {
                getHistoryList()
                searchHistoryStateLiveData.postValue(true)
            }

            false -> {
                searchHistoryStateLiveData.postValue(false)
            }
        }
    }

    fun onSearchBarFocusChangeListener(state: Boolean) {
        setSearchHistoryState(state)
    }

    fun onSearchTextChanged(state: Boolean) {
        setSearchHistoryState(state)
    }

    fun onClearButtonChangeListener(state: Boolean) {
        setSearchHistoryState(state)
    }

    fun historyClearButtonChangeListener(state: Boolean) {
        setSearchHistoryState(state)
    }

    fun showSearchErrorChangeChangeListener(state: Boolean) {
        setSearchHistoryState(state)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}



