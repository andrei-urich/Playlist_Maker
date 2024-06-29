package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.Track

interface SearchConsumer {
        fun consume(tracks: List<Track>)
    }
