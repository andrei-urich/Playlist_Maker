package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track

interface TrackSearchInteractor {
    fun search(request: String, consumer: SearchConsumer<List<Track>>)
}
