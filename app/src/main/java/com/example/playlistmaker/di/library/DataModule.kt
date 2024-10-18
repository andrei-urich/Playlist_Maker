package com.example.playlistmaker.di.library

import androidx.room.Room
import com.example.playlistmaker.data.db.favorite.AppDatabase
import com.example.playlistmaker.data.db.playlist.PlaylistsDatabase
import com.example.playlistmaker.data.db.tracks.AddedTrackDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        Room.databaseBuilder(
            androidContext(),
            PlaylistsDatabase::class.java,
            "playlist_database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AddedTrackDatabase::class.java,
            "added_tracks_database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

}
