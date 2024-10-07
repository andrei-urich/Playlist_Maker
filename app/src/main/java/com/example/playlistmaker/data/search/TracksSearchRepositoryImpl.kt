package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.search.Resource
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.search.TracksSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksSearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDatabase
) :
    TracksSearchRepository {
    override fun search(request: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(request))
        when (response.resultCode) {
            in 200..299 -> {
                if (response is TracksResponse) {
                    val list = checkTheTracksParamsToNull(response.results)
                    val listOfFavoritesIds = database.getTrackDao().getTracksIds()
                    val tracks_ = list.map {
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
                            it.previewUrl!!,
                            false
                        )
                    }
                    val tracks =
                        tracks_.map { track -> checkFavoriteList(track, listOfFavoritesIds) }
                    emit(Resource.Success(tracks))
                } else emit(Resource.Error(response.resultCode))
            }

            else -> emit(Resource.Error(response.resultCode))
        }
    }

    private fun checkTheTracksParamsToNull(list: List<TrackDTO>): List<TrackDTO> {
        val checkedList = list as MutableList<TrackDTO>
        for (i in checkedList.indices) {
            if (checkedList[i].previewUrl.isNullOrBlank()) checkedList.removeAt(i)
        }
        return checkedList
    }

    private fun checkFavoriteList(track: Track, listOfFavoritesIds: List<Int>): Track {
        if (track.trackId in listOfFavoritesIds) track.isFavorite = true
        return track
    }
}