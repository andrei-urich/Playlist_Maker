package com.example.playlistmaker

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

class Audioplayer : AppCompatActivity() {

    lateinit var track: Track
    lateinit var trackImage: ImageView
    lateinit var trackName: TextView
    lateinit var artistName: TextView
    lateinit var btnAddToPlaylist: ImageView
    lateinit var btnPlay: ImageView
    lateinit var btnLike: ImageView
    lateinit var trackProgress: TextView
    lateinit var trackTimeValue: TextView
    lateinit var collectionNameValue: TextView
    lateinit var releaseDateValue: TextView
    lateinit var primaryGenreNameValue: TextView
    lateinit var countryValue: TextView
    lateinit var collectionGroup: Group
    lateinit var releaseDateGroup: Group
    lateinit var primaryGenreNameGroup: Group
    lateinit var countryGroup: Group


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


        val intent = intent
        val trackInfo = intent.getStringExtra(TRACK_INFO)
        track = Gson().fromJson(trackInfo, Track::class.java)
        putOnTrack(track)

        toolbar.setOnClickListener {
            finish()
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
            releaseDateValue.setText(track.releaseDate.substring(0,4))
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
}