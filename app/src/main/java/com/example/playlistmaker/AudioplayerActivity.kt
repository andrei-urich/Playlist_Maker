package com.example.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.appcompat.content.res.AppCompatResources

class AudioplayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT

    private lateinit var track: Track
    private lateinit var trackImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var btnAddToPlaylist: ImageView
    private lateinit var btnPlay: ImageView
    private lateinit var btnLike: ImageView
    private lateinit var trackProgress: TextView
    private lateinit var trackTimeValue: TextView
    private lateinit var collectionNameValue: TextView
    private lateinit var releaseDateValue: TextView
    private lateinit var primaryGenreNameValue: TextView
    private lateinit var countryValue: TextView
    private lateinit var collectionGroup: Group
    private lateinit var releaseDateGroup: Group
    private lateinit var primaryGenreNameGroup: Group
    private lateinit var countryGroup: Group

    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val toolbar = findViewById<Toolbar>(R.id.tbPlayer)
        trackImage = findViewById(R.id.trackImage)
        trackName = findViewById((R.id.trackName))
        artistName = findViewById(R.id.artistName)
        btnAddToPlaylist = findViewById((R.id.btnAddToPlaylist))
        btnPlay = findViewById((R.id.btnPlay))
        btnLike = findViewById(R.id.btnLike)
        trackProgress = findViewById(R.id.trackProgress)
        trackTimeValue = findViewById(R.id.trackTimeValue)
        collectionNameValue = findViewById(R.id.collectionNameValue)
        releaseDateValue = findViewById(R.id.releaseDateValue)
        primaryGenreNameValue = findViewById(R.id.primaryGenreNameValue)
        countryValue = findViewById(R.id.countryValue)
        collectionGroup = findViewById(R.id.collectionGroup)
        releaseDateGroup = findViewById(R.id.releaseDateGroup)
        primaryGenreNameGroup = findViewById(R.id.primaryGenreNameGroup)
        countryGroup = findViewById(R.id.countryGroup)

        toolbar.setOnClickListener {
            finish()
        }

        val intent = intent
        val trackInfo = intent.getStringExtra(TRACK_INFO)
        track = Gson().fromJson(trackInfo, Track::class.java)
        preparePlayer(track)

        btnPlay.setOnClickListener {
            playbackControl()
        }
        putOnTrack(track)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPlay.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_pause))
        playerState = STATE_PLAYING
    }


    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlay.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_play))
        playerState = STATE_PAUSED
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btnPlay.isClickable = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            btnPlay.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.btn_play))
            playerState = STATE_PREPARED
        }
    }

    private fun putOnTrack(track: Track) {
        trackName.setText(track.trackName)
        artistName.setText(track.artistName)
        trackTimeValue.setText(
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis)
        )

        if (!track.collectionName.isNullOrBlank()) {
            collectionNameValue.setText(track.collectionName)
        } else {
            collectionGroup.visibility = View.GONE
        }
        if (!track.releaseDate.isNullOrBlank()) {
            releaseDateValue.setText(track.releaseDate.substring(0, 4))
        } else {
            releaseDateGroup.visibility = View.GONE
        }
        if (!track.primaryGenreName.isNullOrBlank()) {
            primaryGenreNameValue.setText(track.primaryGenreName)
        } else {
            primaryGenreNameGroup.visibility = View.GONE
        }
        if (!track.country.isNullOrBlank()) {
            countryValue.setText(track.country)
        } else {
            countryGroup.visibility = View.GONE
        }

        Glide.with(trackImage)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_big)
            .fitCenter()
            .transform(RoundedCorners(8))
            .dontAnimate()
            .into(trackImage)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}