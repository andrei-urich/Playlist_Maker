package com.example.playlistmaker.data.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesService: PlaylistAPI) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        try {
            if (dto is TracksSearchRequest) {
                val response = withContext(Dispatchers.IO) { iTunesService.search(dto.request) }
                return Response().apply {  resultCode =response.resultCode}
            } else {
                return Response().apply { resultCode = 400 }

            }
        } catch (e: Exception) {
            return Response().apply { resultCode = -1 }
        }
    }
}
