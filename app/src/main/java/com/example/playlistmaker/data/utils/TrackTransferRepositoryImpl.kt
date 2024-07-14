package com.example.playlistmaker.data.utils

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.repository.TrackTransferRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackTransferRepositoryImpl : TrackTransferRepository {
    override fun getTrack(trackInfo: String): Track {
        return Gson().fromJson(trackInfo, Track::class.java)
    }

    override fun sendTrackList(list: MutableList<Track>): String {
        return Gson().toJson(list)
    }

    override fun sendTrack(track: Track): String {
        return Gson().toJson(track)
    }

    override fun getTrackList(trackInfo: String): MutableList<Track> {
        val typeToken = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson<MutableList<Track>>(trackInfo, typeToken).toMutableList()
    }
}
