package com.example.playlistmaker.presentation.search

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

class SearchViewModel(
    private val trackSearchInteractor: TrackSearchInteractor = Creator.provideTrackSearchInteractor(),
    private val searchHistoryInteractor: SearchHistoryInteractor = Creator.provideSearchHistoryInteractor()
) : ViewModel() {

    private var searchStateLiveData = MutableLiveData<TrackSearchState>()
    private var playTrackTrigger = SingleEventLiveData<Track>()

    fun getSearchStateLiveData(): LiveData<TrackSearchState> = searchStateLiveData
    fun getPlayTrackTrigger(): LiveData<Track> = playTrackTrigger


    fun request(request: String) {
        if (request.isNotEmpty()) {
            searchStateLiveData.postValue(TrackSearchState.Loading)
            trackSearchInteractor.search(
                request,
                consumer = object : SearchConsumer<List<Track>> {
                    override fun consume(data: ConsumerData<List<Track>>) {
                        when (data) {
                            is ConsumerData.Error -> searchStateLiveData.postValue(TrackSearchState.Error)
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
        searchHistoryInteractor.addToHistory(track)
        playTrackTrigger.postValue(track)
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

    companion object {
        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel()
                }
            }
        }
    }
}



