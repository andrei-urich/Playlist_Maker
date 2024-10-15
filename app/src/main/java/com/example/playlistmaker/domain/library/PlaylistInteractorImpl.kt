package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        repository.addTrackToPlaylist(track, playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun saveImageToExternalStorage(playlist: Playlist) {
        repository.saveImageToExternalStorage(playlist)
    }

    override fun getTrackIdListAsInt(playlist: Playlist): MutableList<Int> {
        return repository.getTrackIdListAsInt(playlist)
    }

    override fun getTrackIdListAsString(trackIdList: MutableList<Int>): String {
        return repository.getTrackIdListAsString(trackIdList)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun clear() {
        repository.clear()
    }
}