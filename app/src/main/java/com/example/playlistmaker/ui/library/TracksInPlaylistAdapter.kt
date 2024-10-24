package com.example.playlistmaker.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.SearchResultItemBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.SearchTrackViewHolder

class TracksInPlaylistAdapter(
    val tracks: List<Track>,
    private val onItemClick: (Track) -> Unit,
    private val onItemLongClick: (Track) -> Unit
) :
    RecyclerView.Adapter<PlaylistTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val binding =
            SearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlaylistTrackViewHolder(binding, { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                tracks.getOrNull(position)?.let<Track, Unit> { item ->
                    onItemClick(item)
                }
            }
        }, { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                tracks.getOrNull(position)?.let { item ->
                    onItemLongClick(item)
                }
            }
        })
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        tracks.getOrNull(position)?.let<Track, Unit> { track ->
            holder.bind(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}