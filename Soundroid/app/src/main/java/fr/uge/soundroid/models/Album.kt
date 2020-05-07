package fr.uge.soundroid.models

data class Album (val name: String, val albumPicture: String? = null, val artist: Artist? = null){

    val soundtracks: HashMap<Int, Soundtrack> = HashMap()

    fun addSoundtrackList(list: List<Soundtrack>): Album {
        for ( soundtrack in list ) {
            soundtracks[soundtrack.hashCode()] = soundtrack
        }

        return this
    }

    fun addSoundtrack(soundtrack: Soundtrack): Album {
        soundtracks[soundtrack.hashCode()] = soundtrack
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