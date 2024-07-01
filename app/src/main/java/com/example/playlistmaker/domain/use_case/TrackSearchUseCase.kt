package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.SearchConsumer

interface TrackSearchUseCase {
    fun search(expression: String, consumer: SearchConsumer)
}
