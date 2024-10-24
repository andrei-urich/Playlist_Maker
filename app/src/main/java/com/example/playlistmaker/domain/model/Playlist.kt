package com.example.playlistmaker.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Playlist(
    val base_id: Int,
    val name: String,
    val description: String?,
    val cover: String?,
    var trackIds: String?,
    var tracksCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(base_id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(cover)
        parcel.writeString(trackIds)
        parcel.writeInt(tracksCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }

}
