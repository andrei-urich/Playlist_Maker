package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.utils.PLAY_DEBOUNCE_DELAY
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.player.AudioplayerPlayState
import com.example.playlistmaker.domain.player.PlayerState.STATE_PAUSED
import com.example.playlistmaker.domain.player.PlayerState.STATE_PLAYING
import com.example.playlistmaker.domain.player.PlayerState.STATE_PREPARED
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.PlayerState.STATE_COMPLETE
import com.example.playlistmaker.presentation.player.AudioplayerViewModel
import com.example.playlistmaker.ui.mapper.ImageLinkFormatter
import com.example.playlistmaker.ui.mapper.TrackTimeFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import android.view.Gravity
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController

class AudioplayerFragment() : Fragment() {

    private var playerState = "STATE_DEFAULT"
    private lateinit var track: Track
    private val viewModel: AudioplayerViewModel by viewModel() {
        parametersOf(track)
    }

    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!
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
    private lateinit var bottomSheetAdapter: BottomSheetAdapter
    private lateinit var bottomSheetRecyclerView: RecyclerView
    private lateinit var btnNewPlaylist: Button
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val args: AudioplayerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.tbPlayer
        trackImage = binding.trackImage
        trackName = binding.trackName
        artistName = binding.artistName
        btnAddToPlaylist = binding.btnAddToPlaylist
        btnPlay = binding.btnPlay
        btnLike = binding.btnLike
        trackProgress = binding.trackProgress
        trackTimeValue = binding.trackTimeValue
        collectionNameValue = binding.collectionNameValue
        releaseDateValue = binding.releaseDateValue
        primaryGenreNameValue = binding.primaryGenreNameValue
        countryValue = binding.countryValue
        collectionGroup = binding.collectionGroup
        releaseDateGroup = binding.releaseDateGroup
        primaryGenreNameGroup = binding.primaryGenreNameGroup
        countryGroup = binding.countryGroup

        if (requireActivity().findViewById<BottomNavigationView>(R.id.bnView) != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bnView).visibility = View.GONE
        }

        bottomSheetRecyclerView = binding.bottomSheetRv
        bottomSheetRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        btnNewPlaylist = binding.bnNewPlaylist

        val bottomSheetContainer = binding.playlistsBottomSheet
        val overlay = binding.overlay

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        toolbar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        track = args.track
        putOnTrack(track)

        btnPlay.setOnClickListener {
            viewModel.getAction()
        }

        binding.btnLike.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        btnAddToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.getPlaylistsList()
        }

        btnNewPlaylist.setOnClickListener {
            val action =
                AudioplayerFragmentDirections.actionAudioplayerFragmentToAddPlaylistFragment(track)
            findNavController().navigate(action)
        }


        viewModel.getFavoriteStateLiveData().observe(viewLifecycleOwner) {
            btnLikeSwitcher(it)
        }

        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) {
            renderPlaylistState(it)
        }

        viewModel.getTrackToPlaylistLiveData().observe(viewLifecycleOwner) { it ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            overlay.visibility = View.GONE
            showMessage(it)
        }

        viewModel.getPlayStatusLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AudioplayerPlayState.Prepared -> {
                    btnPlay.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.btn_play
                        )
                    )
                    playerState = STATE_PREPARED
                    showTrackPlayedTime()
                }

                is AudioplayerPlayState.Playing -> {
                    btnPlay.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.btn_pause
                        )
                    )
                    playerState = STATE_PLAYING
                    showTrackPlayedTime()
                }

                is AudioplayerPlayState.Paused -> {
                    btnPlay.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.btn_play
                        )
                    )
                    playerState = STATE_PAUSED
                    showTrackPlayedTime()
                }

                is AudioplayerPlayState.Complete -> {
                    btnPlay.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.btn_play
                        )
                    )
                    playerState = STATE_COMPLETE
                    showTrackPlayedTime()
                }
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

    }


    private fun renderPlaylistState(list: List<Playlist>?) {
        if (!list.isNullOrEmpty()) {
            bottomSheetRecyclerView.visibility = View.VISIBLE
            bottomSheetAdapter = BottomSheetAdapter(list, this::addTrackToPlaylist)
            bottomSheetRecyclerView.adapter = bottomSheetAdapter
            bottomSheetAdapter.notifyDataSetChanged()
        }
    }


    private fun addTrackToPlaylist(playlist: Playlist) {
        viewModel.addTrackToPlaylist(playlist, track)
    }

    private fun showTrackPlayedTime() {
        when (playerState) {
            STATE_PLAYING -> {
                timerJob?.cancel()
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
        btnLikeSwitcher(track.isFavorite)
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

    private fun btnLikeSwitcher(isFavorite: Boolean) {
        when (isFavorite) {
            true -> {
                binding.btnLike.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireActivity(),
                        R.drawable.btn_like_on
                    )
                )
            }

            else -> {
                binding.btnLike.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireActivity(),
                        R.drawable.btn_like_off
                    )
                )
            }
        }
    }

    private fun showMessage(it: Pair<Boolean, String>) {
        var messageText = ""
        if (it.first) {
            messageText =
                context?.getString(R.string.audioplayer_add_to_playlist_message_true) + " " + it.second
        } else {
            messageText =
                context?.getString(R.string.audioplayer_add_to_playlist_message_false) + " " + it.second
        }

        val snackbar = Snackbar.make(
            binding.root, messageText,
            Snackbar.LENGTH_LONG
        )
        val snackbarView = snackbar.view
        context?.getColor(R.color.text_primary)?.let { snackbarView.setBackgroundColor(it) }
        val textView: TextView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text)
        context?.getColor(R.color.primary)?.let { textView.setTextColor(it) }
        textView.setTextAppearance(R.style.snake_text_style)
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
        textView.setGravity(Gravity.CENTER)
        snackbar.show()
    }


    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}