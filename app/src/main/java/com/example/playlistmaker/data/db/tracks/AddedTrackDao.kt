package com.example.playlistmaker.data.db.tracks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AddedTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(addedTrackEntity: AddedTrackEntity)

    @Delete
    suspend fun deleteTrack(addedTrackEntity: AddedTrackEntity)

    @Query("SELECT * FROM added_tracks")
    suspend fun getTracks(): List<AddedTrackEntity>

    @Query("DELETE FROM added_tracks WHERE track_id = :trackId")
    suspend fun deleteById(trackId: Int)
}