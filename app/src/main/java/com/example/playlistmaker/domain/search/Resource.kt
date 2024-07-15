package com.example.playlistmaker.domain.search


sealed interface Resource<T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error<T>(val resultCode: Int) : Resource<T>
}
