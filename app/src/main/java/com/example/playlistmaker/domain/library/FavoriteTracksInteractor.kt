package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun addTrackToFavorite(track: Track)
    suspend fun removeTrackFromFavorite(track: Track)
    fun getFavoriteTracksList(): Flow<List<Track>>
}