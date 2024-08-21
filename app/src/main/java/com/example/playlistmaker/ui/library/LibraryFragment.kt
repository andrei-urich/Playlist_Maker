package com.example.playlistmaker.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator


class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!


    private lateinit var fragmentList: List<Fragment>
    private lateinit var fragmentListTitles: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentListTitles = getTabsTitle()
        fragmentList = getFragmentList()

        val adapter = PagerAdapter(requireActivity(), fragmentList)
        binding.pager.adapter = adapter
        TabLayoutMediator(binding.tlTab, binding.pager) { tab, pos ->
            tab.setText(fragmentListTitles[pos])
        }.attach()
        binding.pager.currentItem = 1

    }

    override fun onResume() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bnView).visibility=View.VISIBLE
        super.onResume()
    }

    fun getFragmentList(): List<Fragment> {
        return listOf(
            FavoriteTracksFragment.newInstance(),
            PlaylistsFragment.newInstance()
        )
    }

    fun getTabsTitle(): List<String> {
        return listOf(
            this.getString(R.string.favorite_tracks_tab_title),
            this.getString(R.string.playlists_tab_title)
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

