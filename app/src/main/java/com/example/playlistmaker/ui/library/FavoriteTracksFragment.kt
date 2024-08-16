package com.example.playlistmaker.ui.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.presentation.library.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

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

        viewModel.getLiveData().observe(viewLifecycleOwner) {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}