package com.example.playlistmaker.domain.search

interface SearchConsumer <T> {
        fun consume(data: ConsumerData<T>)
    }
