package com.example.playlistmaker.ui.player

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetListItemBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.Formatter


class BottomSheetViewHolder(
    private val binding: BottomSheetListItemBinding, onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        val descriptionText =
            playlist.tracksCount.toString() + " " + Formatter.formatRu(playlist.tracksCount)
        binding.playlistDescription.text = descriptionText

        val coverUri = playlist.cover?.toUri()

        Glide.with(binding.playlistCover)
            .load(coverUri)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(2)
            )
            .dontAnimate().into(binding.playlistCover)
    }
}
