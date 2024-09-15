package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TracksSearchRepository) :
    TrackSearchInteractor {
    override fun search(request: String): Flow<Pair<List<Track>?, Int?>> {
        return repository.search(request).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.resultCode)
                }
            }
        }
    }
}
