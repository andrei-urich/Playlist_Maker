package com.example.playlistmaker.ui.library
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator


class LibraryActivity : AppCompatActivity() {

    lateinit var binding: ActivityLibraryBinding

    private lateinit var fragmentList: List<Fragment>
    private lateinit var fragmentListTitles: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentListTitles = getTabsTitle()
        fragmentList = getFragmentList()

        val toolbar = binding.tbLib
        toolbar.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        val adapter = PagerAdapter(this, fragmentList)
        binding.pager.adapter = adapter
        TabLayoutMediator(binding.tlTab, binding.pager) { tab, pos ->
            tab.setText(fragmentListTitles[pos])
        }.attach()
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
}

