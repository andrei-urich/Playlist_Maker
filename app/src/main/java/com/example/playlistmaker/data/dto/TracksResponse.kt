package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.model.Track

data class TracksResponse (val results: ArrayList<TrackDTO>) : Response()