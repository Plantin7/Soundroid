package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.Playlist
import io.realm.Realm
import io.realm.RealmResults

object PlaylistRepository {
    
    /* TODO : close instance after service utilisation */
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    init {
        Log.i(PlaylistRepository::class.java.name,"Playlist service created")
    }

    /**
     * THis function save the given Playlists in the database.
     */
    fun savePlaylistList(elements: List<Playlist>) {
        realm.beginTransaction()
        for ( element in elements ) {
            realm.copyToRealmOrUpdate(element)
        }
        realm.commitTransaction()
    }

    /**
     * This function save the given Playlist in the database.
     */
    fun savePlaylist(element: Playlist) {
        savePlaylistList(
            listOf(
                element
            )
        )
    }

    /**
     * This function execute the realm request with the given conditions.
     * It returns the RealmResults (A list that contains all the founded elements)
     *
     * @return RealmResults<Playlist> the query result.
     */
    private fun findPlaylists(conditions: Map<String, String>): RealmResults<Playlist> {
        val query = realm.where<Playlist>(Playlist::class.java)

        conditions.forEach {
            query.equalTo(it.key, it.value)
        }

        return query.findAll()
    }

    /**
     * This function return the Playlist founded with the given conditions.
     * If more or less than one Playlist was founded the function return null.
     *
     * @return The founded Playlist.
     */
    fun findSinglePlaylist(conditions: Map<String, String>): Playlist? {
        val results =
            findPlaylists(
                conditions
            )
        if ( conditions.size != 1 ) return null;
        return results.first()
    }

    /**
     * This function return the founded Playlists in the database with the given conditions.
     *
     * @return The founded Playlists list.
     */
    fun findPlaylistsList(conditions: Map<String, String>): List<Playlist> {
        return findPlaylists(
            conditions
        ).toList()
    }

    /**
     * This function return all the playlists of the database.
     *
     * @return The founded Playlists list.
     */
    fun findAll(): List<Playlist> {
        return findPlaylists(HashMap()).toList()
    }
}