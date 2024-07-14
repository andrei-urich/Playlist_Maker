package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.OnPlayerStateChangeListener
import com.example.playlistmaker.domain.player.PlayerState.STATE_COMPLETE
import com.example.playlistmaker.domain.player.PlayerState.STATE_PREPARED
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl : PlayerRepository {

    val mediaPlayer = MediaPlayer()
    private lateinit var listener: OnPlayerStateChangeListener

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun preparePlayer(
        track: Track,
        listener: OnPlayerStateChangeListener
    ) {
        this.listener = listener
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener.onChange(STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            listener.onChange(STATE_COMPLETE)
        }
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayer.currentPosition)
    }

    override fun release() {
        mediaPlayer.release()
    }


}