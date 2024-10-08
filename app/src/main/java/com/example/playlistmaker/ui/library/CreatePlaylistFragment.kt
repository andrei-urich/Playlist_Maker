package com.example.playlistmaker.ui.library

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.presentation.library.CreatePlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePlaylistViewModel by viewModels()
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bnView).visibility =
            View.GONE
        toolbar = binding.tbCreatePlaylist

        toolbar.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.bnView).visibility =
                View.VISIBLE
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

}