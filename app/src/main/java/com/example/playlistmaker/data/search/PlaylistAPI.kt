package com.example.playlistmaker.data.search

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistAPI {
    @GET("search?entity=song")
    suspend fun search(
        @Query("term") text: String
    ): TracksResponse
}
