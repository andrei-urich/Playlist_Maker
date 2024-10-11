package com.example.playlistmaker.ui.library

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.model.Playlist

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding

) : RecyclerView.ViewHolder(binding.root) {

    private val name = binding.name
    private val description = binding.description

    fun bind(playlist: Playlist) {
        name.text = playlist.name
        description.text = playlist.description
        val coverUri = playlist.cover?.toUri()

        Glide.with(binding.cover)
            .load(coverUri)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(8))
            .dontAnimate()
            .into(binding.cover)
    }
}
