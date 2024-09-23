package com.example.playlistmaker.data.search

data class TrackDTO(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    var trackTime: String?,
    val artworkUrl100: String,
    val trackTimeMillis: Long,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)
