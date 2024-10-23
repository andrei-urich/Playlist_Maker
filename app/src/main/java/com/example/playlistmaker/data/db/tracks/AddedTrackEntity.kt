package com.example.playlistmaker.data.db.tracks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "added_tracks",
    indices = [Index("track_id", unique = true)]
)
data class AddedTrackEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "base_id")
    val base_id: Int,
    @ColumnInfo(name = "track_id")
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean
)

