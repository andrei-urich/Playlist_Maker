package com.example.playlistmaker.domain.search

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val resultCode: Int) : ConsumerData<T>
}