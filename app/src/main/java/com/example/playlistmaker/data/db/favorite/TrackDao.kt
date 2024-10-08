package com.example.playlistmaker.data.db.favorite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Delete
    suspend fun deleteTrack(trackId: TrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT track_id FROM favorite_tracks")
    suspend fun getTracksIds(): List<Int>

    @Query("DELETE FROM favorite_tracks WHERE track_id = :trackId")
    suspend fun deleteById(trackId: Int)

}