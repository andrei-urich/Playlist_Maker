package com.example.playlistmaker.data.db.tracks

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [AddedTrackEntity::class])
abstract class AddedTrackDatabase : RoomDatabase() {
    abstract fun getAddedTrackDao(): AddedTrackDao
}