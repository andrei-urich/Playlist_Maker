package com.example.playlistmaker.ui.repository

import com.example.playlistmaker.domain.model.Track

interface TrackTransferRepository {
    fun getTrack(trackInfo: String): Track
    fun sendTrack(track: Track): String
}