package com.example.playlistmaker.data.utils

import com.example.playlistmaker.data.db.tracks.AddedTrackEntity
import com.example.playlistmaker.domain.model.Track

class AddedTrackDbConverter {
    fun map(track: Track): AddedTrackEntity {
        return AddedTrackEntity(
            0,
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

    fun map(addedTrackEntity: AddedTrackEntity): Track {
        return Track(
            addedTrackEntity.trackId,
            addedTrackEntity.trackName,
            addedTrackEntity.artistName,
            "",
            addedTrackEntity.artworkUrl100,
            addedTrackEntity.trackTimeMillis,
            addedTrackEntity.collectionName,
            addedTrackEntity.releaseDate,
            addedTrackEntity.primaryGenreName,
            addedTrackEntity.country,
            addedTrackEntity.previewUrl,
            addedTrackEntity.isFavorite
        )
    }
}