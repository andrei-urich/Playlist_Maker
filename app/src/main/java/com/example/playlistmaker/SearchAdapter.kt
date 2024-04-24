package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private val tracks: List<Track>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TrackViewHolder).bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }


}