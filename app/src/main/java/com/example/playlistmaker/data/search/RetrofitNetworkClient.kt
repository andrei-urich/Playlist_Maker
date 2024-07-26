package com.example.playlistmaker.data.search

class RetrofitNetworkClient (private val iTunesService: PlaylistAPI) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            val resp = iTunesService.search(dto.request).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }

        }
    }
}
