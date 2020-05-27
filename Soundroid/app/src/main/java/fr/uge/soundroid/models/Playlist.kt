package fr.uge.soundroid.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.lang.AssertionError

open class Playlist(
    @PrimaryKey
    var id: Int? = null,

    @Required
    var title: String? = null) : RealmObject(), SoundroidSearchable {

    var soundtracks = RealmList<Soundtrack>()

    fun addSoundtrack(soundtrack: Soundtrack): Playlist {
        soundtracks.add(soundtrack)
        return this
    }

    fun addSoundtrackList(list: List<Soundtrack>): Playlist {
        for ( soundtrack in list ) {
            addSoundtrack(soundtrack)
        }

        return this
    }

    fun soundtrackNumber(): Int {
        return soundtracks.size;
    }

    fun hasSoundtracks(): Boolean {
        return soundtracks.size > 0
    }

    override fun getNameForSearch(): String {
        return if ( title != null ) {
            "Playlist - " + title!!
        } else {
            "Unknown playlist name"
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (id != other.id) return false
        if (title != other.title) return false
        //if (soundtracks != other.soundtracks) return false

        return true
    }

    override fun hashCode(): Int {
        return title.hashCode()
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
                "\"soundtracks\": ["

        for ( soundtrack in soundtracks ) {
            s += "{\"id\": ${soundtrack.id}},"
        }
        s += "]}"

        return s
    }
}