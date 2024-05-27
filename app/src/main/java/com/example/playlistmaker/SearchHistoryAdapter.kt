package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SearchHistoryAdapter(val searchHistoryTracks: List<Track>) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var onItemClick: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)
        return TrackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchHistoryTracks[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(searchHistoryTracks[position])
        }
    }

    override fun getItemCount(): Int {
        return searchHistoryTracks.size
    }
}
