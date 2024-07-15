package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track

interface TracksSearchRepository {
    fun search(request: String): Resource<List<Track>>
}
