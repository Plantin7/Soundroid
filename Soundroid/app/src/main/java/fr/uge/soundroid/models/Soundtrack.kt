package fr.uge.soundroid.models

import android.util.Log
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.lang.AssertionError


open class Soundtrack(

    @PrimaryKey
    var id: Int? = null,

    @Required
    var title: String? = null,

    @Required
    var path: String? = null,

    @Required
    var duration: Int? = null,

    var note: Float? = null,

    var artist: Artist? = null,

    var album: Album? = null,

    @Required
    var listeningNumber: Int? = 0) : RealmObject(), SoundroidSearchable {

    var tags = RealmList<Tag>()

    fun addTag(tag: Tag): Soundtrack {
        tags.add(tag)
        return this
    }

    fun addTagList(list: List<Tag>): Soundtrack {
        for ( soundtrack in list ) {
            addTag(soundtrack)
        }

        return this
    }

    fun tagNumber(): Int {
        return tags.size;
    }

    fun hasTag(): Boolean {
        return tags.size > 0
    }

    override fun getNameForSearch(): String {
        return if ( title != null ) {
            "Song - " + title!!
        } else {
            "Unknown Soundtrack title"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Soundtrack

        if (id != other.id) return false
        if (title != other.title) return false
        if (path != other.path) return false
        if (duration != other.duration) return false
        if (note != other.note ) return false
        if (artist != other.artist) return false
        if (album != other.album) return false

        return true
    }

    override fun hashCode(): Int {
        return "$title $path $duration ${artist?.name} ${album?.name}".hashCode()
    }

    fun initPrimaryKey(): Int {
        id = hashCode()
        if ( id != null ) {
            return id!!
        } else {
            throw AssertionError()
        }
    }

    fun exportToJson(): String {
        var s = "{" +
                "\"id\": $id," +
                "\"title\": \"$title\"," +
                "\"path\": \"$path\"," +
                "\"duration\": $duration," +
                "\"note\": $note," +
                "\"artist\": ${artist?.id}," +
                "\"album\": ${album?.id}," +
                "\"tags\": ["

        for ( tag in tags ) {
            s += "{\"id\": ${tag.id}},"
        }

        s += "]}"

        return s
    }
}