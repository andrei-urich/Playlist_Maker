package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.comsumers.ConsumerData
import com.example.playlistmaker.domain.comsumers.SearchConsumer
import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TracksSearchRepository
import com.example.playlistmaker.domain.use_case.TrackSearchInteractor
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TracksSearchRepository) :
    TrackSearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(request: String, consumer: SearchConsumer<List<Track>>) {
        executor.execute {

            when (val searchTracksResponse = repository.search(request)) {
                is Resource.Success -> {
                    val tracks = searchTracksResponse.data
                    consumer.consume(ConsumerData.Data(tracks))
                }

                is Resource.Error -> {
                    val resultCode = searchTracksResponse.resultCode
                    consumer.consume(ConsumerData.Error(resultCode))
                }
            }
        }
    }
}
