package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trailingButton = itemView.findViewById<ImageView>(R.id.trailing_icon)
    private val trackName = itemView.findViewById<TextView>(R.id.trackName)
    private val artistName = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime = itemView.findViewById<TextView>(R.id.trackTime)
    private val trackImage = itemView.findViewById<ImageView>(R.id.track_image)

    fun bind(track: Track) {
        trackName.setText(track.trackName)
        artistName.setText(track.artistName)
        trackTime.setText(track.trackTime)

        if (isOnline(itemView.context)) {
            Glide.with(trackImage)
                .load(track.artworkUrl100)
                .into(trackImage)
        } else trackImage.setImageResource(R.drawable.placeholder)

    }
}