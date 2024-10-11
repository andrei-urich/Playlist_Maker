package com.example.playlistmaker.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.model.Playlist

class PlaylistAdapter(
    val playlists: List<Playlist>
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        playlists.getOrNull(position)?.let { playlist ->
            holder.bind(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}
