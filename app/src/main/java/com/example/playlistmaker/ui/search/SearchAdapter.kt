package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.SearchResultItemBinding
import com.example.playlistmaker.domain.model.Track

class SearchAdapter(
    val tracks: List<Track>,
    private val onItemClick: (Track) -> Unit
) :
    RecyclerView.Adapter<SearchTrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val binding =
            SearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchTrackViewHolder(binding) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                tracks.getOrNull(position)?.let<Track, Unit> { track ->
                    onItemClick(track)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        tracks.getOrNull(position)?.let<Track, Unit> { track ->
            holder.bind(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }


}