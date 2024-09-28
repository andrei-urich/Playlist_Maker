package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksInteractorImpl(
    private val repository: FavoriteTracksRepository
) : FavoriteTracksInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        repository.addTrackToFavorite(track)
    }

    override suspend fun removeTrackFromFavorite(track: Track) {
        repository.removeTrackFromFavorite(track)
    }

    override suspend fun getFavoriteTracksList(): List<Track> {
        return repository.getFavoriteTracksList().reversed()
    }

//    override fun getFavoriteTracksList(): Flow<List<Track>> {
//        return repository.getFavoriteTracksList().map { it.reversed() }
//    }


    override suspend fun checkInFavorite(track: Track): Boolean {
        return repository.checkInFavorite(track)
    }
}