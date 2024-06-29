package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.SearchConsumer
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.use_case.TrackSearch
import java.util.concurrent.Executors

class TrackSearchImpl(private val repository: TracksRepository) : TrackSearch {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: SearchConsumer) {
        executor.execute {
            consumer.consume(repository.search(expression))
        }
    }
}
