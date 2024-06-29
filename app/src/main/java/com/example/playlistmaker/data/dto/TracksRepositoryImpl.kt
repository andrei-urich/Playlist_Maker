package com.example.playlistmaker.data.dto

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.domain.ResultCode
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TracksRepository
import java.text.SimpleDateFormat
import java.util.Locale


class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {

    override fun search(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        ResultCode.resultCode=response.resultCode
        if (response.resultCode == 200) {
            return (response as TracksResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTime,
                    it.artworkUrl100,
                    it.trackTimeMillis,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}
