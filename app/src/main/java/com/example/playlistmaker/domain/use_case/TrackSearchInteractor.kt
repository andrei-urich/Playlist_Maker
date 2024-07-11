package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.comsumers.SearchConsumer
import com.example.playlistmaker.domain.model.Track

interface TrackSearchInteractor {
    fun search(request: String, consumer: SearchConsumer <List<Track>>)
}
