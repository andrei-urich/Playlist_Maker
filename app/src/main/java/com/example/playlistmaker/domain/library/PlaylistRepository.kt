package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun saveImageToExternalStorage(playlist: Playlist)
    fun getTrackIdListAsInt(playlist: Playlist): MutableList<Int>
    fun getTrackIdListAsString(trackIdList: MutableList<Int>): String
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun clear()
    suspend fun getTrackById(trackId: Int): Track?
    suspend fun getPlaylistById(playlistId: Int): Playlist?
}