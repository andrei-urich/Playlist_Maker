package com.example.playlistmaker.domain.model

data class Track(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    var trackTime: String?, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackTimeMillis: Long?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)