package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.ResourceResponseResult
import com.example.playlistmaker.domain.ResourceResponseResult.ERROR
import com.example.playlistmaker.domain.ResourceResponseResult.SUCCESS
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TracksRepository


class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {

    override fun search(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            ResourceResponseResult.resourceResponseResult = SUCCESS
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
            ResourceResponseResult.resourceResponseResult = ERROR
            return emptyList()
        }
    }
}
