package com.example.playlistmaker.data.library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.data.db.playlist.PlaylistEntity
import com.example.playlistmaker.data.db.playlist.PlaylistsDatabase
import com.example.playlistmaker.data.db.tracks.AddedTrackDatabase
import com.example.playlistmaker.data.utils.AddedTrackDbConverter
import com.example.playlistmaker.data.utils.PlaylistDbConvertor
import com.example.playlistmaker.domain.library.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackTransferRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val database: PlaylistsDatabase,
    private val addedTrackDatabase: AddedTrackDatabase,
    private val converter: PlaylistDbConvertor,
    private val addedTrackDbConverter: AddedTrackDbConverter,
    private val trackTransfer: TrackTransferRepository,
    val context: Context
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        database.getPlaylistDao().insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database.getPlaylistDao().deletePlaylist(converter.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = database.getPlaylistDao().getPlaylists()
        emit(convertToPlaylist(playlists))
    }

    override suspend fun saveImageToExternalStorage(playlist: Playlist) {
        val uri = playlist.cover!!.toUri()
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), playlist.name)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, playlist.name + ".jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        withContext(Dispatchers.IO) {
            outputStream.close()
        }
    }

    override fun getTrackIdListAsInt(playlist: Playlist): MutableList<Int> {
        return trackTransfer.getTrackIdList(playlist)
    }

    override fun getTrackIdListAsString(trackIdList: MutableList<Int>): String {
        return trackTransfer.setTrackIdList(trackIdList)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        database.getPlaylistDao().updatePlaylist(converter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        addedTrackDatabase.getAddedTrackDao().insertTrack(addedTrackDbConverter.map(track))

    }

    override suspend fun clear() {
        database.getPlaylistDao().clear()
    }

    override suspend fun getTrackById(trackId: Int): Track? {
        val _track = addedTrackDatabase.getAddedTrackDao().getTrackById(trackId)
        if (_track != null) {
            return addedTrackDbConverter.map(_track)
        }
        return null
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist? {
        val playlist = database.getPlaylistDao().getPlaylistById(playlistId)
        if (playlist != null) {
            return converter.map(playlist)
        }
        return null
    }


    private fun convertToPlaylist(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { entity -> converter.map(entity) }
    }
}