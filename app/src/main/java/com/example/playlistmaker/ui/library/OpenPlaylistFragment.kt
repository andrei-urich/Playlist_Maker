package com.example.playlistmaker.ui.library

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentOpenPlaylistBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.library.OpenPlaylistViewModel
import com.example.playlistmaker.ui.Formatter
import com.example.playlistmaker.ui.search.SearchAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.core.parameter.parametersOf

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
    private val viewModel: OpenPlaylistViewModel by viewModel() {
        parametersOf(playlist)
    }
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

        playlist = args.playlist
        setPlaylist(playlist)

        toolbar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }



        bottomSheetMenuBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        //                   overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //                   overlay.visibility = View.VISIBLE
                    }

                    else -> {
                        //                   overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        bottomSheetTrackListBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        //                   overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //                   overlay.visibility = View.VISIBLE
//                        bottomSheetRecyclerView.getLayoutParams().height =
//                            bottomSheetRecyclerView.getHeight() - bottomSheet.getHeight()
//                        bottomSheetRecyclerView.requestLayout()
                    }

                    else -> {
                        //                   overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun setPlaylist(playlist: Playlist) {
        title.text = (playlist.name)
        description.text = (playlist.description)
        if (playlist.tracksCount == 0) {
            TODO()
        } else {
            trackList.clear()
            trackList = viewModel.getTrackList(playlist)
            bottomSheetAdapter = SearchAdapter(trackList, viewModel::playTrack)
            bottomSheetRecyclerView.adapter = bottomSheetAdapter
            bottomSheetAdapter.notifyDataSetChanged()

            val durationString = calculateDuration(trackList)
            duration.text = durationString
            val tracksCountString =
                playlist.tracksCount.toString() + " " + Formatter.formatTracks(playlist.tracksCount)
            trackCount.text = tracksCountString
            bottomSheetTrackListBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val cover = playlist.cover?.toUri()
        Glide.with(playlistCover)
            .load(cover)
            .placeholder(R.drawable.placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(8))
            .dontAnimate()
            .into(playlistCover)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


    private fun calculateDuration(trackList: List<Track>): String {
        var durationInMills: Long = 0L
        for (track in trackList) {
            if (track.trackTimeMillis != null) {
                durationInMills += track.trackTimeMillis
            }
        }
        val durationInt = durationInMills.toInt()
        val pluralsString = Formatter.formatMinutes(durationInt)
        val string = durationInt.toString() + " " + pluralsString
        return string
    }
}