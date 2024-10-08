package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val repository: FavoriteTracksRepository
) : FavoriteTracksInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        repository.addTrackToFavorite(track)
    }

    override suspend fun removeTrackFromFavorite(track: Track) {
        repository.removeTrackFromFavorite(track)
    }

    override suspend fun deleteTrackById(trackId: Int) {
        repository.deleteTrackById(trackId)
    }

    override fun getFavoriteTracksList(): Flow<List<Track>> {
        return repository.getFavoriteTracksList() }


    override suspend fun checkInFavorite(track: Track): Boolean {
        return repository.checkInFavorite(track)
    }
}