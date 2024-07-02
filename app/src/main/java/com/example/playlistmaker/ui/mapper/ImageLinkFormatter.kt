package com.example.playlistmaker.ui.mapper

object ImageLinkFormatter {
    fun formatPlayingTrackImageLink(trackImageLink: String): String {
        return trackImageLink.replaceAfterLast('/', "512x512bb.jpg")
    }
}