package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchInteractor {
    fun search(request: String): Flow<Pair<List<Track>?, Int?>>
}
