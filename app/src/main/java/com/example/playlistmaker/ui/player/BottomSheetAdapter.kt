package com.example.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.BottomSheetListItemBinding
import com.example.playlistmaker.domain.model.Playlist

class BottomSheetAdapter(
    val list: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
) : RecyclerView.Adapter<BottomSheetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val binding =
            BottomSheetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BottomSheetViewHolder(binding) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                list.getOrNull(position)?.let { playlist ->
                    onItemClick(playlist)
                }
            }
        }
    }


    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        list.getOrNull(position)?.let<Playlist, Unit> { playlist ->
            holder.bind(playlist)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}
