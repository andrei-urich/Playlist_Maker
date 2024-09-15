package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksSearchRepository {
    fun search(request: String): Flow<Resource<List<Track>>>
}
