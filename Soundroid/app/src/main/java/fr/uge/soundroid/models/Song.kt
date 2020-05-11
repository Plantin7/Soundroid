package fr.uge.soundroid.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

// TODO Change type of the duration variable
data class Song(val albumPicture: String?, val title:String?, val artiste:String?, val duration:String?, val songPath:String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(albumPicture)
        parcel.writeString(title)
        parcel.writeString(artiste)
        parcel.writeString(duration)
        parcel.writeString(songPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}