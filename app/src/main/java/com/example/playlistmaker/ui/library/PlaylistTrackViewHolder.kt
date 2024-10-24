package com.example.playlistmaker.ui.library

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SearchResultItemBinding
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistTrackViewHolder(
    private val binding: SearchResultItemBinding,
    onItemClick: (position: Int) -> Unit,
    onItemLongClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onItemClick(bindingAdapterPosition)
        }
        itemView.setOnLongClickListener {
            onItemLongClick(bindingAdapterPosition)
            true
        }
    }

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis)
        binding.trackImage.setImageURI(null)
        Glide.with(binding.trackImage.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(2))
            .dontAnimate()
            .into(binding.trackImage)
    }
}


