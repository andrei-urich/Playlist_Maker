package com.example.playlistmaker.data.utils

import android.util.Log
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackTransferRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackTransferRepositoryImpl : TrackTransferRepository {

    override fun getTrack(trackInfo: String): Track {
        return Gson().fromJson(trackInfo, Track::class.java)
    }

    override fun getTrackIdList(playlist: Playlist): MutableList<Int> {
        Log.d("MY", playlist.trackIds.toString())
        val typeToken = object : TypeToken<MutableList<Int>>() {}.type
        if (playlist.trackIds.isNullOrBlank()) return mutableListOf<Int>() else return Gson().fromJson<MutableList<Int>>(
            playlist.trackIds,
            typeToken
        ).toMutableList()
    }

    override fun setTrackIdList(trackList: MutableList<Int>): String {
        return Gson().toJson(trackList)
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
