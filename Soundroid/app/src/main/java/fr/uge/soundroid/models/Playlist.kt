package fr.uge.soundroid.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Playlist(
    @PrimaryKey
    var id: Int? = null,

    @Required
    var title: String? = null) : RealmObject() {

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
}