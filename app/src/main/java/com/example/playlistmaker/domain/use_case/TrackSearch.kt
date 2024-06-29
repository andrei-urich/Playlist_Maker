package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.SearchConsumer

interface TrackSearch {
    fun search(expression: String, consumer: SearchConsumer)
}
