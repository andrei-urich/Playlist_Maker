package com.example.playlistmaker.ui.library

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentOpenPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.library.OpenPlaylistViewModel
import com.example.playlistmaker.utils.Formatter
import com.example.playlistmaker.ui.search.SearchAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar

class OpenPlaylistFragment : Fragment() {

    private var _binding: FragmentOpenPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var toolbar: Toolbar
    private lateinit var playlistCover: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var duration: TextView
    private lateinit var trackCount: TextView
    private lateinit var shareButton: ImageView
    private lateinit var menuButton: ImageView
    private lateinit var bottomSheetAdapter: SearchAdapter
    private lateinit var bottomSheetRecyclerView: RecyclerView
    private lateinit var menuOptionShare: TextView
    private lateinit var menuOptionEdit: TextView
    private lateinit var menuOptionDelete: TextView
    private lateinit var bottomSheetMenuBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetMenuContainer: ConstraintLayout
    private lateinit var bottomSheetTrackListBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetTrackContainer: LinearLayout
    private val args: OpenPlaylistFragmentArgs by navArgs()
    private lateinit var playlist: Playlist
    private val viewModel: OpenPlaylistViewModel by viewModel()
    private var trackList = mutableListOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpenPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireActivity().findViewById<BottomNavigationView>(R.id.bnView) != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bnView).visibility = View.GONE
        }

        toolbar = binding.tbOpenPlaylist
        playlistCover = binding.playlistImage
        title = binding.tvTitle
        description = binding.tvDescription
        duration = binding.tvDuration
        trackCount = binding.tvTracksCount
        shareButton = binding.ivShare
        menuButton = binding.menu
        menuOptionShare = binding.menuOption1
        menuOptionEdit = binding.menuOption2
        menuOptionDelete = binding.menuOption3
        bottomSheetMenuContainer = binding.menuBottomSheet
        bottomSheetTrackContainer = binding.tracksBottomSheet

        bottomSheetRecyclerView = binding.tracksBottomSheetRv
        bottomSheetRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        bottomSheetTrackListBehavior = BottomSheetBehavior.from(bottomSheetTrackContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetMenuBehavior = BottomSheetBehavior.from(bottomSheetMenuContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.getTrackListLiveData().observe(viewLifecycleOwner) {
            renderTrackList(it)
        }
        viewModel.getPlayTrackTrigger().observe(viewLifecycleOwner) {
            playTrack(it)
        }

        viewModel.getShareIntentLiveData().observe(viewLifecycleOwner) {
            if (!it) showSnake(it)
        }

        playlist = args.playlist
        setPlaylist(playlist)

        toolbar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        shareButton.setOnClickListener {
            viewModel.sharePlaylist(playlist, trackList)
        }
        menuButton.setOnClickListener {
            bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.overlay.visibility = View.VISIBLE
        }


        bottomSheetMenuBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        bottomSheetTrackContainer.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.overlay.visibility = View.VISIBLE
                        bottomSheetTrackContainer.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        bottomSheetTrackContainer.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        bottomSheetTrackListBehavior.peekHeight =
            binding.openPlRootLayout.getHeight() - binding.openPlConstrLayout.getHeight()

        bottomSheetTrackListBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        bottomSheet.layoutParams.height =
                            binding.openPlRootLayout.getHeight() - binding.openPlConstrLayout.getHeight()
                        bottomSheetRecyclerView.requestLayout()
                    }

                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun playTrack(it: Track) {
        val action =
            OpenPlaylistFragmentDirections.actionOpenPlaylistFragmentToAudioplayerFragment(it)
        findNavController().navigate(action)
    }

    private fun renderTrackList(it: List<Track>) {
        trackList.clear()
        trackList = it.toMutableList()
        bottomSheetAdapter = SearchAdapter(trackList, viewModel::playTrack)
        bottomSheetRecyclerView.adapter = bottomSheetAdapter
        bottomSheetAdapter.notifyDataSetChanged()

        duration.text = calculateDuration(trackList)

        val tracksCountString =
            playlist.tracksCount.toString() + " " + Formatter.formatTracks(playlist.tracksCount)
        trackCount.text = tracksCountString

    }

    private fun setPlaylist(playlist: Playlist) {
        title.text = (playlist.name)
        description.text = (playlist.description)
        if (playlist.tracksCount == 0) {
            binding.tvEmpty.visibility = View.VISIBLE
            duration.text = requireActivity().getString(R.string.empty_playlist_duration)
            trackCount.text = requireActivity().getString(R.string.empty_playlist_tracks_count)
        } else {
            binding.tvEmpty.visibility = View.GONE
            viewModel.getTrackList(playlist)
        }
        bottomSheetTrackContainer.visibility = View.VISIBLE
        bottomSheetMenuContainer.visibility = View.VISIBLE
        bottomSheetTrackListBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        if (playlist.cover?.isNotBlank() == true) {
            val cover = playlist.cover.toUri()
            playlistCover.setImageURI(null)
            Glide.with(playlistCover.context)
                .load(cover)
                .centerCrop()
                .dontAnimate().into(playlistCover)
        }
    }
    private fun showSnake(it: Boolean) {
        if (!it) {
            val text = requireActivity().getString(R.string.empty_playlist_share_warning)
            val snackbar = Snackbar.make(
                binding.root, text,
                Snackbar.LENGTH_SHORT
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
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTrackList(playlist)
    }

    private fun calculateDuration(trackList: List<Track>): String {
        var durationInMills: Long = 0L
        for (track in trackList) {
            if (track.trackTimeMillis != null) {
                durationInMills += track.trackTimeMillis
            }
        }
        val durationInt = (durationInMills / 60000).toInt()
        val pluralsString = Formatter.formatMinutes(durationInt)
        val string = durationInt.toString() + " " + pluralsString
        return string
    }
}