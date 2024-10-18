package com.example.playlistmaker.ui.library

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.Formatter

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding

) : RecyclerView.ViewHolder(binding.root) {

    private val name = binding.name
    private val description = binding.description

    fun bind(playlist: Playlist) {
        name.text = playlist.name
        val descriptionText =
            playlist.tracksCount.toString() + " " + Formatter.formatRu(playlist.tracksCount)
        description.text = descriptionText

        val coverUri = playlist.cover?.toUri()

        Glide.with(binding.cover)
            .load(coverUri)
            .placeholder(R.drawable.placeholder)
            .dontAnimate()
            .into(binding.cover)


    }
}
