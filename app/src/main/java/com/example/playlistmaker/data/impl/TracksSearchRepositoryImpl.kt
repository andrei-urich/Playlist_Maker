package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.domain.model.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TracksSearchRepository


class TracksSearchRepositoryImpl(private val networkClient: NetworkClient) :
    TracksSearchRepository {

    override fun search(request: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(request))
        return if (response is TracksResponse) {
            val tracks = response.results.map {
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
            Resource.Success(tracks)
        } else {
            Resource.Error(response.resultCode)
        }
    }
}
