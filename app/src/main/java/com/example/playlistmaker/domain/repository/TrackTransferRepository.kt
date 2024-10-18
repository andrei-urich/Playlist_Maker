package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

interface TrackTransferRepository {
    fun sendTrackList(list: MutableList<Track>): String
    fun sendTrack(track: Track): String
    fun getTrackList(trackInfo: String): MutableList<Track>
    fun getTrack(trackInfo: String): Track
    fun getTrackIdList(playlist: Playlist): MutableList<Int>
    fun setTrackIdList(trackList: MutableList<Int>): String
}