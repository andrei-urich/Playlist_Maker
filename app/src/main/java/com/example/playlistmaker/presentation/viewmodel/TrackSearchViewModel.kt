package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.comsumers.ConsumerData
import com.example.playlistmaker.domain.comsumers.SearchConsumer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.use_case.TrackSearchInteractor
import com.example.playlistmaker.presentation.state.TrackSearchState
import com.example.playlistmaker.presentation.utils.SingleEventLiveData

class TrackSearchViewModel(
    private val searchInteractor: TrackSearchInteractor = Creator.provideTracksSearchInteractor()
) : ViewModel() {

    private var searchStateLiveData = MutableLiveData<TrackSearchState>()
    private var playTrackTrigger = SingleEventLiveData<Track>()
    private var historyLiveData = MutableLiveData<List<Track>>()

    fun getSearchStateLiveData(): LiveData<TrackSearchState> = searchStateLiveData
    fun getPlayTrackTrigger(): LiveData<Track> = playTrackTrigger
    fun getHistoryLiveData(): LiveData<List<Track>> = historyLiveData

    fun request(request: String) {
        if (request.isNotEmpty()) {
            searchStateLiveData.postValue(TrackSearchState.Loading)
            searchInteractor.search(
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
        playTrackTrigger.postValue(track)
    }
}


