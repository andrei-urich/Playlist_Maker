package com.example.playlistmaker.ui.mapper

import java.text.SimpleDateFormat
import java.util.Locale

object TrackTimeFormatter {
 fun formatTime (trackTimeMillis: Long) : String {
     return SimpleDateFormat(
         "mm:ss",
         Locale.getDefault()
     ).format(trackTimeMillis)
 }
}