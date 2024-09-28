package com.example.playlistmaker.data.library

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.data.utils.TrackDbConverter
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.library.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val database: AppDatabase,
    private val converter: TrackDbConverter
) : FavoriteTracksRepository {

    override suspend fun addTrackToFavorite(track: Track) {
        database.getTrackDao().insertTrack(converter.map(track))
    }

    override suspend fun removeTrackFromFavorite(track: Track) {
        database.getTrackDao().deleteTrack(converter.map(track))
    }

    override suspend fun getFavoriteTracksList(): List<Track> {
        val tracksEntities = database.getTrackDao().getTracks()
        return convertToTracks(tracksEntities)
    }

//    override fun getFavoriteTracksList(): Flow<List<Track>> = flow {
//        val tracksEntities = database.getTrackDao().getTracks()
//        emit(convertToTracks(tracksEntities))
//    }

    override suspend fun checkInFavorite(track: Track): Boolean {
        val tracksEntities = convertToTracks(database.getTrackDao().getTracks())
        val idList = tracksEntities.map { it.trackId}
        return (track.trackId in idList)
    }


    private fun convertToTracks(trackEntityList: List<TrackEntity>): List<Track> {
        return trackEntityList.map { entity -> converter.map(entity) }
    }
}