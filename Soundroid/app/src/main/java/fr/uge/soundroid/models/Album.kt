package fr.uge.soundroid.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import fr.uge.soundroid.R
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

open class Album (
    @PrimaryKey
    var id: Int? = null,

    @Required
    var name: String? = null,

    var albumPicture: String? = null,

    var artist: Artist? = null ) : RealmObject() {

    var soundtracks = RealmList<Soundtrack>()

    fun addSoundtrack(soundtrack: Soundtrack): Album {
        soundtracks.add(soundtrack)
        return this
    }

    fun addSoundtrackList(list: List<Soundtrack>): Album {
        for ( soundtrack in list ) {
            addSoundtrack(soundtrack)
        }

        return this
    }

    fun soundtrackNumber(): Int {
        return soundtracks.size;
    }

    fun hasArtist(): Boolean {
        return artist != null
    }

    fun hasSoundtracks(): Boolean {
        return soundtracks.size > 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Album

        if (id != other.id) return false
        if (name != other.name) return false
        if (albumPicture != other.albumPicture) return false
        if (artist != other.artist) return false
        if (soundtracks != other.soundtracks) return false

        return true
    }

    override fun hashCode(): Int {
        return "$name $albumPicture ${artist?.name}".hashCode()
    }

    fun initPrimaryKey(): Int {
        id = hashCode()
        if ( id != null ) {
            return id!!
        } else {
            throw AssertionError()
        }
    }

    companion object {
        @Ignore
        val map:MutableMap<Int?, Bitmap> = HashMap()
        // cache lru
    }

    fun getBitmap(context: Context): Bitmap? {
        if (map[id] == null) {
            map[id] = try {
                MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(albumPicture))
            } catch (e: FileNotFoundException) {
                BitmapFactory.decodeResource(context.resources, R.mipmap.kissclipart)
            }
        }
        return map[id]
    }
}