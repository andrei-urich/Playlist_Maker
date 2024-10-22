package com.example.playlistmaker.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.SearchTrackViewHolder

class PlaylistAdapter(
    val playlists: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                playlists.getOrNull(position)?.let<Playlist, Unit> { item ->
                    onItemClick(item)
                }
            }
        }
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
