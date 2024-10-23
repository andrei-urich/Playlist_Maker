package com.example.playlistmaker.domain.library

import android.net.Uri
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

    override suspend fun saveImageToExternalStorage(playlist: Playlist) : Uri {
    return repository.saveImageToExternalStorage(playlist)
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

    override fun getTrackById(trackIds: List<Int>): Flow<List<Track>> {
        return repository.getTrackById(trackIds)
    }

    override fun getPlaylistById(playlistId: Int): Flow<Playlist> {
        return repository.getPlaylistById(playlistId)
    }
}