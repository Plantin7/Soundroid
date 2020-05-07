package fr.uge.soundroid.models

data class Soundtrack(val title: String, val path: String, val seconds: Int, val artist: Artist? = null, val album: Album? = null) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        /* TODO : create a standard hash */
        return super.hashCode()
    }
}