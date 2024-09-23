package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.example.playlistmaker.utils.PLAY_DEBOUNCE_DELAY
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.player.AudioplayerPlayState
import com.example.playlistmaker.domain.player.PlayerState.STATE_PAUSED
import com.example.playlistmaker.domain.player.PlayerState.STATE_PLAYING
import com.example.playlistmaker.domain.player.PlayerState.STATE_PREPARED
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerState.STATE_COMPLETE
import com.example.playlistmaker.presentation.player.AudioplayerViewModel
import com.example.playlistmaker.ui.mapper.ImageLinkFormatter
import com.example.playlistmaker.ui.mapper.TrackTimeFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioplayerActivity : AppCompatActivity() {

    private var playerState = "STATE_DEFAULT"
    private lateinit var track: Track
    private val viewModel: AudioplayerViewModel by viewModel() {
        parametersOf(track)
    }
    private lateinit var viewBinding: ActivityAudioplayerBinding
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
    private var timerJob: Job? = null

    private val args: AudioplayerActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAudioplayerBinding.inflate(this.layoutInflater)
        setContentView(viewBinding.root)

        val toolbar = viewBinding.tbPlayer
        trackImage = viewBinding.trackImage
        trackName = viewBinding.trackName
        artistName = viewBinding.artistName
        btnAddToPlaylist = viewBinding.btnAddToPlaylist
        btnPlay = viewBinding.btnPlay
        btnLike = viewBinding.btnLike
        trackProgress = viewBinding.trackProgress
        trackTimeValue = viewBinding.trackTimeValue
        collectionNameValue = viewBinding.collectionNameValue
        releaseDateValue = viewBinding.releaseDateValue
        primaryGenreNameValue = viewBinding.primaryGenreNameValue
        countryValue = viewBinding.countryValue
        collectionGroup = viewBinding.collectionGroup
        releaseDateGroup = viewBinding.releaseDateGroup
        primaryGenreNameGroup = viewBinding.primaryGenreNameGroup
        countryGroup = viewBinding.countryGroup

        toolbar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        track = args.track
        putOnTrack(track)

        btnPlay.setOnClickListener {
            viewModel.getAction()
        }

        viewModel.getPlayStatusLiveData().observe(this) { state ->
            when (state) {
                is AudioplayerPlayState.Prepared -> {
                    btnPlay.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.btn_play
                        )
                    )
                    playerState = STATE_PREPARED
                    showTrackPlayedTime()
                }

                is AudioplayerPlayState.Playing -> {
                    btnPlay.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.btn_pause
                        )
                    )
                    playerState = STATE_PLAYING
                    showTrackPlayedTime()
                }

                is AudioplayerPlayState.Paused -> {
                    btnPlay.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.btn_play
                        )
                    )
                    playerState = STATE_PAUSED
                    showTrackPlayedTime()
                }

                is AudioplayerPlayState.Complete -> {
                    btnPlay.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.btn_play
                        )
                    )
                    playerState = STATE_COMPLETE
                    showTrackPlayedTime()
                }
            }
        }
    }

    private fun showTrackPlayedTime() {
        when (playerState) {
            STATE_PLAYING -> {
                timerJob = lifecycleScope.launch {
                    while (playerState == STATE_PLAYING) {
                        delay(PLAY_DEBOUNCE_DELAY)
                        trackProgress.setText(
                            viewModel.getCurrentPosition()
                        )
                    }
                }
            }

            STATE_PAUSED -> {
                timerJob?.cancel()
                if (!viewModel.getCurrentPosition().equals(R.string.blankTimer)) {
                    trackProgress.setText(
                        viewModel.getCurrentPosition()
                    )
                }
            }

            STATE_PREPARED -> {
                timerJob?.cancel()
            }

            STATE_COMPLETE -> {
                timerJob?.cancel()
                trackProgress.setText(R.string.blankTimer)
            }
        }
    }

    private fun putOnTrack(track: Track) {
        trackName.setText(track.trackName)
        artistName.setText(track.artistName)
        trackTimeValue.setText(
            TrackTimeFormatter.formatTime(track.trackTimeMillis)
        )

        if (!track.collectionName.isNullOrBlank()) {
            collectionNameValue.setText(
                if (track.collectionName.length <= 26) track.collectionName else (track.collectionName.substring(
                    0,
                    23
                ) + "...")
            )
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
            .load(ImageLinkFormatter.formatPlayingTrackImageLink(track.artworkUrl100))
            .placeholder(R.drawable.placeholder_big)
            .fitCenter()
            .transform(RoundedCorners(8))
            .dontAnimate()
            .into(trackImage)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onStop() {
        super.onStop()
        if (isChangingConfigurations) {
            onRetainNonConfigurationInstance()
        }
    }
}