package com.example.playlistmaker.data.search

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitNetworkClient : NetworkClient {
    val iTunesBaseUrl = "https://itunes.apple.com"
    val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val iTunesService = retrofit.create<PlaylistAPI>()

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
