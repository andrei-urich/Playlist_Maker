package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.SearchConsumer
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.use_case.TrackSearchUseCase
import java.util.concurrent.Executors

class TrackSearchUseCaseImpl(private val repository: TracksRepository) : TrackSearchUseCase {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: SearchConsumer) {
        executor.execute {
            consumer.consume(repository.search(expression))
        }
    }
}
