package com.example.playlistmaker.data.library

import com.example.playlistmaker.data.db.playlist.PlaylistEntity
import com.example.playlistmaker.data.db.playlist.PlaylistsDatabase
import com.example.playlistmaker.data.utils.PlaylistDbConvertor
import com.example.playlistmaker.domain.library.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val database: PlaylistsDatabase,
    private val converter: PlaylistDbConvertor
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        database.getPlaylistDao().insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database.getPlaylistDao().deletePlaylist(converter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(trackId: Int, playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = database.getPlaylistDao().getPlaylists()
        emit(convertToPlaylist(playlists))
    }

    private fun convertToPlaylist(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { entity -> converter.map(entity) }
    }

}