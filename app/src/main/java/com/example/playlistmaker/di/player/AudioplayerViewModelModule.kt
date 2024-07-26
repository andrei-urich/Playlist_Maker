package com.example.playlistmaker.di.player

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.player.AudioplayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioplayerViewModelModule = module {
    viewModel { (track: Track) ->
        AudioplayerViewModel(track, get())
    }
}