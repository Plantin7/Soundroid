package fr.uge.soundroid.models

data class Playlist(val title: String) {

    val soundtracks: HashMap<Int, Soundtrack> = HashMap()

    fun addSoundtrackList(list: List<Soundtrack>): Playlist {
        for ( soundtrack in list ) {
            soundtracks[soundtrack.hashCode()] = soundtrack
        }

        return this
    }

    fun addSoundtrack(soundtrack: Soundtrack): Playlist {
        soundtracks[soundtrack.hashCode()] = soundtrack
        return this
    }

    fun soundtrackNumber(): Int {
        return soundtracks.size;
    }

    fun hasSoundtracks(): Boolean {
        return soundtracks.size > 0
    }
}