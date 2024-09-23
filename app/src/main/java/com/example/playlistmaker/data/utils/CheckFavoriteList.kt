package com.example.playlistmaker.data.utils

import com.example.playlistmaker.domain.model.Track

fun checkFavoriteList(track: Track, listOfFavoritesIds: List<Int>): Track {
    track.isFavorite = track.trackId in listOfFavoritesIds
    return track
}