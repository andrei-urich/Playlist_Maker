package com.example.playlistmaker.data.search


interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

}