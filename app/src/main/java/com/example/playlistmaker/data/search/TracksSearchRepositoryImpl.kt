package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.search.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksSearchRepository


class TracksSearchRepositoryImpl(private val networkClient: NetworkClient) :
    TracksSearchRepository {
    override fun search(request: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(request))
        if (response is TracksResponse) {
            val list = checkTheTracksParamsToNull(response.results)
            val tracks = list.map {
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
                    it.previewUrl!!
                )
            }
            return Resource.Success(tracks)
        } else return Resource.Error(response.resultCode)
    }

    private fun checkTheTracksParamsToNull(list: List<TrackDTO>): List<TrackDTO> {
        val checkedList = list as MutableList<TrackDTO>
        for (i in checkedList.indices) {
            if (checkedList[i].previewUrl.isNullOrBlank()) checkedList.removeAt(i)
        }
        return checkedList
    }
}