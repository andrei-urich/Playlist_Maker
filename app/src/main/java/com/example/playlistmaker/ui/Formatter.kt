package com.example.playlistmaker.ui

object Formatter {
    fun formatTracks(num: Int): String {
        val rightCase = arrayOf("трек", "трека", "треков")
        val num100: Int = num % 100
        return if (num100 in 5..20) rightCase[2]
        else {
            val num10 = num100 % 10
            when (num10) {
                1 -> rightCase[0]
                in 2..4 -> rightCase[1]
                else -> rightCase[2]
            }
        }
    }

    fun formatMinutes(num: Int): String {
        val rightCase = arrayOf("минута", "минуты", "минут")
        val num100: Int = num % 100
        return if (num100 in 5..20) rightCase[2]
        else {
            val num10 = num100 % 10
            when (num10) {
                1 -> rightCase[0]
                in 2..4 -> rightCase[1]
                else -> rightCase[2]
            }
        }
    }
}