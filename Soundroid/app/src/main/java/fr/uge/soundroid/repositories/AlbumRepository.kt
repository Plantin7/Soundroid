package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.Album
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

object AlbumRepository {
    
    /* TODO : close instance after service utilisation */
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    init {
        Log.i(AlbumRepository::class.java.name,"Album service created")
    }

    /**
     * THis function save the given Albums in the database.
     */
    fun saveAlbumList(elements: List<Album>) {
        realm.beginTransaction()
        for ( element in elements ) {
            realm.copyToRealmOrUpdate(element)
        }
        realm.commitTransaction()
    }

    /**
     * This function save the given Album in the database.
     */
    fun saveAlbum(element: Album) {
        saveAlbumList(
            listOf(
                element
            )
        )
    }

    /**
     * This function execute the realm request with the given conditions.
     * It returns the RealmResults (A list that contains all the founded elements)
     *
     * @return RealmResults<Album> the query result.
     */
    private fun findAlbums(conditions: Map<String, String>, sorted: String = "name", order: Boolean = true): RealmResults<Album> {
        val query = realm.where<Album>(Album::class.java)

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
     * @return RealmResults<Album> the query result.
     */
    private fun findAlbumsLike(conditions: Map<String, String>): RealmResults<Album> {
        val query = realm.where<Album>(Album::class.java)

        conditions.forEach {
            query.like(it.key, "*${it.value}*", Case.INSENSITIVE)
        }

        return query.findAll()
    }

    /**
     * This function return the Album founded with the given conditions.
     * If more or less than one Album was founded the function return null.
     *
     * @return The founded Album.
     */
    fun findSingleAlbum(conditions: Map<String, String>): Album? {
        val results =
            findAlbums(
                conditions
            )
        if ( conditions.size != 1 ) return null;
        return results.first()
    }

    /**
     * This function return the founded Albums in the database with the given conditions.
     *
     * @return The founded Albums list.
     */
    fun findAlbumsList(conditions: Map<String, String>): List<Album> {
        return findAlbums(
            conditions
        ).toList()
    }

    /**
     * This function return all the albums of the database.
     *
     * @return The founded Album list.
     */
    fun findAll(): List<Album> {
        return findAlbums(HashMap()).toList()
    }

    /**
     * This function return the albums filtered by the given parameters.
     *
     * @return The founded Album list.
     */
    fun findLike(filters: Map<String, String>): List<Album> {
        return findAlbumsLike(
            filters
        ).toList()
    }

    /**
     * This function delete all the albums founded with the given conditions.
     */
    fun deleteAlbumsByConditions(conditions: Map<String, String>) {
        realm.executeTransaction {
            findAlbums(conditions).deleteAllFromRealm()
        }
    }

    /**
     * This function delete a single album with the given conditions.
     *
     * @return Return true if the album was founded and deleted, false either.
     */
    fun deleteSingleAlbumByConditions(conditions: Map<String, String>): Boolean {
        val result = findSingleAlbum(conditions) ?: return false

        realm.executeTransaction {
            result.deleteFromRealm()
        }

        return true
    }

    /**
     * This function delete all the albums of the database.
     */
    fun deleteAllAlbums() {
        realm.executeTransaction {
            findAlbums(HashMap()).deleteAllFromRealm()
        }
    }

    /**
     * This function delete the given list of albums.
     */
    fun deleteAlbumsList(albums: List<Album>) {
        realm.executeTransaction {
            for ( album in albums ) {
                album.deleteFromRealm()
            }
        }
    }

    /**
     * This function delete the given album.
     */
    fun deleteAlbum(album: Album) {
        deleteAlbumsList(listOf(album))
    }

    fun findAlbumById(id: Int): Album? {
        val query = realm.where<Album>(Album::class.java)
        query.equalTo("id", id)
        return query.findFirst()
    }
}