package com.example.playlistmaker.data.search


interface NetworkClient {
    fun doRequest(dto: Any): Response

}