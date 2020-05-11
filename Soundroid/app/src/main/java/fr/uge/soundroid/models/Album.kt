package fr.uge.soundroid.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

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
}