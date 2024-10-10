package com.example.playlistmaker.data.library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.data.db.playlist.PlaylistEntity
import com.example.playlistmaker.data.db.playlist.PlaylistsDatabase
import com.example.playlistmaker.data.utils.PlaylistDbConvertor
import com.example.playlistmaker.domain.library.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val database: PlaylistsDatabase,
    private val converter: PlaylistDbConvertor,
    val context: Context
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


    private fun convertToPlaylist(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { entity -> converter.map(entity) }
    }

}