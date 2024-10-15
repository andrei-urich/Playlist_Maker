package com.example.playlistmaker.ui.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.library.FavoriteTracksViewModel
import com.example.playlistmaker.ui.search.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private var tracks = mutableListOf<Track>()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: SearchAdapter
    private lateinit var action: NavDirections
    private val viewModel: FavoriteTracksViewModel by viewModel()
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding: FragmentFavoriteTracksBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getPlayTrackTrigger().observe(viewLifecycleOwner) { track ->
            playTrack(track)
        }

    }

    private fun playTrack(track: Track) {
        action =
            LibraryFragmentDirections.actionLibraryFragmentToAudioplayerActivity(track)
        findNavController().navigate(
            action
        )
    }

    private fun render(list: List<Track>) {
        binding.progressBar.visibility = View.GONE
        if (list.isNullOrEmpty()) {
            tracks.clear()
            recyclerView.visibility = View.GONE
            showPlaceholders(true)

        } else {
            showPlaceholders(false)
            tracks.clear()
            tracks = list.toMutableList()
            adapter = SearchAdapter(tracks, viewModel::playTrack)
            recyclerView.adapter = adapter
            recyclerView.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }

    private fun showPlaceholders(flag: Boolean) {
        if (flag) {
            binding.placeholder.visibility = View.VISIBLE
        } else {
            binding.placeholder.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getFavoriteList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}