package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.Playlist
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

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
    private fun findPlaylists(conditions: Map<String, String>, sorted: String = "title", order: Boolean = true): RealmResults<Playlist> {
        val query = realm.where<Playlist>(Playlist::class.java)

        conditions.forEach {
            query.equalTo(it.key, it.value)
        }

        val sorting: Sort

        if ( order ) {
            sorting = Sort.ASCENDING
        } else {
            sorting = Sort.DESCENDING
        }

        query.sort(sorted, sorting)

        return query.findAll()
    }

    /**
     * This function execute the realm request with the given conditions.
     * It returns the RealmResults (A list that contains all the founded elements)
     *
     * @return RealmResults<Playlist> the query result.
     */
    private fun findPlaylistsLike(conditions: Map<String, String>, filter: String = "title", order: Sort = Sort.ASCENDING): RealmResults<Playlist> {
        val query = realm.where<Playlist>(Playlist::class.java)

        conditions.forEach {
            query.like(it.key, "*${it.value}*", Case.INSENSITIVE)
        }

        query.sort(filter, order)

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

    /**
     * This function return the albums filtered by the given parameters.
     *
     * @return The founded Playlist list.
     */
    fun findLike(filters: Map<String, String>, filter: String = "title", order: Sort = Sort.ASCENDING): List<Playlist> {
        return findPlaylistsLike(
            filters,
            filter,
            order
        ).toList()
    }

    /**
     * This function delete all the playlists founded with the given conditions.
     */
    fun deletePlaylistsByConditions(conditions: Map<String, String>) {
        realm.executeTransaction {
            findPlaylists(conditions).deleteAllFromRealm()
        }
    }

    /**
     * This function return a single playlist with the given conditions.
     *
     * @return Return true if the playlist was founded and deleted, false either.
     */
    fun deleteSinglePlaylistByConditions(conditions: Map<String, String>): Boolean {
        val result = findSinglePlaylist(conditions) ?: return false

        realm.executeTransaction {
            result.deleteFromRealm()
        }

        return true
    }

    /**
     * This function delete all the playlists of the database.
     */
    fun deleteAllPlaylists() {
        realm.executeTransaction {
            findPlaylists(HashMap()).deleteAllFromRealm()
        }
    }

    /**
     * This function delete the given list of playlists.
     */
    fun deletePlaylistsList(playlists: List<Playlist>) {
        realm.executeTransaction {
            for ( playlist in playlists ) {
                playlist.deleteFromRealm()
            }
        }
    }

    /**
     * This function delete the given playlist.
     */
    fun deletePlaylist(playlist: Playlist) {
        deletePlaylistsList(listOf(playlist))
    }

    fun findPlaylistById(id: Int): Playlist? {
        val query = realm.where<Playlist>(Playlist::class.java)
        query.equalTo("id", id)
        return query.findFirst()
    }
}