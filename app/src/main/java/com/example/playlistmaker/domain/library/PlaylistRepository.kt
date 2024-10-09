package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(trackId: Int, playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun saveImageToExternalStorage(playlist: Playlist)
}