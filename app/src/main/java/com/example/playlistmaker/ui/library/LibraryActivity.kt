package com.example.playlistmaker.ui.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.playlistmaker.databinding.ActivityLibraryBinding


class LibraryActivity : AppCompatActivity() {

    lateinit var binding: ActivityLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.tbLib
        toolbar.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()

            supportFragmentManager.commit {

            }

        }

    }
}