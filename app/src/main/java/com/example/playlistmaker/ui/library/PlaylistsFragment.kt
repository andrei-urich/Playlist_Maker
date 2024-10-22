package com.example.playlistmaker.ui.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.library.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var button: Button
    private lateinit var action: NavDirections
    private lateinit var adapter: PlaylistAdapter
    private lateinit var recyclerView: RecyclerView
    private var listOfPlaylists = mutableListOf<Playlist>()
    private lateinit var placeholder: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = binding.bnAddPlaylist
        placeholder = binding.placeholderEmptyPlaylists
        recyclerView = binding.rvPlaylist

        adapter = PlaylistAdapter(listOfPlaylists, viewModel::openPlaylists)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.adapter = adapter

        button.setOnClickListener {
            viewModel.createPlaylist()
        }

        viewModel.getLiveData().observe(viewLifecycleOwner) { list ->
            binding.progressBar.visibility = View.GONE
            showPlaceholder(list.isNullOrEmpty())
            showPlaylists(list)
        }

        viewModel.getCreatePlaylistTrigger().observe(viewLifecycleOwner) {
            if (it) {
                action = LibraryFragmentDirections.actionLibraryFragmentToAddPlaylistFragment(null)
                findNavController().navigate(action)
            }
        }

        viewModel.getOpenPlaylistsTrigger().observe(viewLifecycleOwner) {
            openPlaylist(it)
        }
    }

    private fun openPlaylist(it: Playlist) {
        action = LibraryFragmentDirections.actionLibraryFragmentToOpenPlaylistFragment(it)
        findNavController().navigate(action)

    }

    private fun showPlaylists(list: List<Playlist>?) {
        if (list.isNullOrEmpty()) {
            recyclerView.visibility = View.GONE
        } else {
            listOfPlaylists.clear()
            listOfPlaylists.addAll(list)
            recyclerView.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }

    private fun showPlaceholder(flag: Boolean) {
        if (flag) placeholder.visibility = View.VISIBLE else placeholder.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getPlaylistList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}