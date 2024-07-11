package com.example.playlistmaker.domain.comsumers

interface SearchConsumer <T> {
        fun consume(data: ConsumerData<T>)
    }
