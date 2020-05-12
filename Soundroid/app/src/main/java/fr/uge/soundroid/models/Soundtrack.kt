package fr.uge.soundroid.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.lang.AssertionError
import java.lang.IllegalStateException


open class Soundtrack(

    @PrimaryKey
    var id: Int? = null,

    @Required
    var title: String? = null,

    @Required
    var path: String? = null,

    @Required
    var seconds: Int? = null,

    var artist: Artist? = null,

    var album: Album? = null) : RealmObject() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Soundtrack

        if (id != other.id) return false
        if (title != other.title) return false
        if (path != other.path) return false
        if (seconds != other.seconds) return false
        if (artist != other.artist) return false
        if (album != other.album) return false

        return true
    }

    override fun hashCode(): Int {
        return "$title $path $seconds ${artist?.name} ${album?.name}".hashCode()
    }

    fun initPrimaryKey(): Int {
        id = hashCode()
        if ( id != null ) {
            return id!!
        } else {
            throw AssertionError()
        }
    }
}