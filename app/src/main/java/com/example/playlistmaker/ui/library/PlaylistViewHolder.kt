package com.example.playlistmaker.ui.library

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.utils.Formatter

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onItemClick(bindingAdapterPosition)
        }

    }

    private val name = binding.name
    private val description = binding.description

    fun bind(playlist: Playlist) {
        name.text = playlist.name
        val descriptionText =
            playlist.tracksCount.toString() + " " + Formatter.formatTracks(playlist.tracksCount)
        description.text = descriptionText


        val coverUri = playlist.cover?.toUri()
        binding.cover.setImageURI(null)
        Glide.with(binding.cover.context)
            .load(coverUri)
            .placeholder(R.drawable.placeholder)
            .dontAnimate()
            .into(binding.cover)


    }
}
