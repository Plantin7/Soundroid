package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.Artist
import io.realm.Realm
import io.realm.RealmResults

object ArtistRepository {
    
    /* TODO : close instance after service utilisation */
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    init {
        Log.i(ArtistRepository::class.java.name,"Artist service created")
    }

    /**
     * THis function save the given Artists in the database.
     */
    fun saveArtistList(elements: List<Artist>) {
        realm.beginTransaction()
        for ( element in elements ) {
            realm.copyToRealmOrUpdate(element)
        }
        realm.commitTransaction()
    }

    /**
     * This function save the given Artist in the database.
     */
    fun saveArtist(element: Artist) {
        saveArtistList(
            listOf(
                element
            )
        )
    }

    /**
     * This function execute the realm request with the given conditions.
     * It returns the RealmResults (A list that contains all the founded elements)
     *
     * @return RealmResults<Artist> the query result.
     */
    private fun findArtists(conditions: Map<String, String>): RealmResults<Artist> {
        val query = realm.where<Artist>(Artist::class.java)

        conditions.forEach {
            query.equalTo(it.key, it.value)
        }

        return query.findAll()
    }

    /**
     * This function return the Artist founded with the given conditions.
     * If more or less than one Artist was founded the function return null.
     *
     * @return The founded Artist.
     */
    fun findSingleArtist(conditions: Map<String, String>): Artist? {
        val results =
            findArtists(
                conditions
            )
        if ( conditions.size != 1 ) return null;
        return results.first()
    }

    /**
     * This function return the founded Artists in the database with the given conditions.
     *
     * @return The founded Artists list.
     */
    fun findArtistsList(conditions: Map<String, String>): List<Artist> {
        return findArtists(
            conditions
        ).toList()
    }
}