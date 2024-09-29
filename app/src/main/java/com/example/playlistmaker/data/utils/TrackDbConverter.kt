package com.example.playlistmaker.data.utils

import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.domain.model.Track

class TrackDbConverter {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.artworkUrl100,
            track.trackTimeMillis ?: 0,
            track.collectionName ?: "",
            track.releaseDate ?: "",
            track.primaryGenreName ?: "",
            track.country ?: "",
            track.previewUrl ?: "",
            track.isFavorite
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            "",
            trackEntity.artworkUrl100,
            trackEntity.trackTimeMillis,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
            trackEntity.isFavorite
        )
    }

}