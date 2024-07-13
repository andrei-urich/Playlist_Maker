package com.example.playlistmaker.ui.repository

import com.example.playlistmaker.domain.model.Track
import java.lang.reflect.Type

interface TrackTransferRepository {
    fun sendTrackList(list: MutableList<Track>): String
    fun sendTrack(track: Track): String
    fun getTrackList(trackInfo: String): MutableList<Track>
    fun getTrack(trackInfo: String): Track

}