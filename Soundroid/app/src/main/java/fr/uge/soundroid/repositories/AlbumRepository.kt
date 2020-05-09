package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.Album
import io.realm.Realm
import io.realm.RealmResults

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
    private fun findAlbums(conditions: Map<String, String>): RealmResults<Album> {
        val query = realm.where<Album>(Album::class.java)

        conditions.forEach {
            query.equalTo(it.key, it.value)
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
}