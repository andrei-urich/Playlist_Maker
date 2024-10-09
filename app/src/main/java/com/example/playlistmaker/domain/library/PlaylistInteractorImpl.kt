package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl (
    private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(trackId: Int, playlist: Playlist) {
        repository.addTrackToPlaylist(trackId, playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun saveImageToExternalStorage(playlist: Playlist) {
        repository.saveImageToExternalStorage(playlist)
    }
}