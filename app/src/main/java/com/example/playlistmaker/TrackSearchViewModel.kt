package com.example.playlistmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.comsumers.ConsumerData
import com.example.playlistmaker.domain.comsumers.SearchConsumer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.use_case.TrackSearchInteractor

class TrackSearchViewModel(
    private val searchInteractor: TrackSearchInteractor = Creator.provideTracksSearchInteractor()
) : ViewModel() {

    private var searchStateLiveData = MutableLiveData<TrackSearchState>()


    fun getSearchStateLiveData(): LiveData<TrackSearchState> = searchStateLiveData

    private var historyLiveData = MutableLiveData<List<Track>>()
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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val searchInteractor =
                        Creator.provideTracksSearchInteractor()
                    TrackSearchViewModel(
                        searchInteractor
                    )
                }
            }
    }
}
