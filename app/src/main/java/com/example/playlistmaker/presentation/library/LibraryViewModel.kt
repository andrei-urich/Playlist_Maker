package com.example.playlistmaker.presentation.library

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.library.FavoriteTracksFragment
import com.example.playlistmaker.ui.library.PlaylistsFragment

class LibraryViewModel : ViewModel() {

    fun getTabsTitle(context: Context): List<String> {
        return listOf(
            context.getString(R.string.favorite_tracks_tab_title),
            context.getString(R.string.playlists_tab_title)
        )
    }

    fun getFragmentList(): List<Fragment> {
        return listOf(
            FavoriteTracksFragment.newInstance(),
            PlaylistsFragment.newInstance()
        )
    }
}

