package com.example.playlistmaker.ui.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.example.playlistmaker.presentation.library.LibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class LibraryActivity : AppCompatActivity() {

    lateinit var binding: ActivityLibraryBinding

    private val viewModel: LibraryViewModel by viewModel()

    private val fragmentList = viewModel.getFragmentList()
    private val fragmentListTitles = viewModel.getTabsTitle(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}

