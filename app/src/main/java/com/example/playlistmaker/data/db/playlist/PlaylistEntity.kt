package com.example.playlistmaker.data.db.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlists",
    indices = [Index("base_id")]
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "base_id")
    val base_id: Int,
    val name: String,
    val description: String,
    val cover: String,
    val trackIds: String,
    val tracksCount: Int
)
