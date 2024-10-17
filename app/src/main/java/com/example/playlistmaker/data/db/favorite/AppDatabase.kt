package com.example.playlistmaker.data.db.favorite

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 4, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTrackDao(): TracksDao
}