package com.example.playlistmaker.data.utils

import com.example.playlistmaker.data.db.playlist.PlaylistEntity
import com.example.playlistmaker.domain.model.Playlist

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            base_id = 0,
            name = playlist.name,
            description = if (playlist.description.isNullOrBlank()) {
                ""
            } else playlist.description,
            cover = if (playlist.cover.isNullOrBlank()) {
                ""
            } else playlist.cover,
            trackIds = if (playlist.trackIds.isNullOrBlank()) {
                ""
            } else playlist.trackIds,
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            name = playlist.name,
            description = playlist.description,
            cover = playlist.cover,
            trackIds = playlist.trackIds,
            tracksCount = playlist.tracksCount
        )
    }
}