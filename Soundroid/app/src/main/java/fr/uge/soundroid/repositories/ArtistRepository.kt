package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.Artist
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

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
    private fun findArtists(conditions: Map<String, String>, sorted: String = "name", order: Boolean = true): RealmResults<Artist> {
        val query = realm.where<Artist>(Artist::class.java)

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
     * @return RealmResults<Artist> the query result.
     */
    private fun findArtistsLike(conditions: Map<String, String>, filter: String = "name", order: Sort = Sort.ASCENDING): RealmResults<Artist> {
        val query = realm.where<Artist>(Artist::class.java)

        conditions.forEach {
            query.like(it.key, "*${it.value}*", Case.INSENSITIVE)
        }

        query.sort(filter, order)

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

    /**
     * This function return all the soundtracks of the database.
     *
     * @return The founded Soundtrack list.
     */
    fun findAll(): List<Artist> {
        return findArtists(HashMap()).toList()
    }

    /**
     * This function return the artists filtered by the given parameters.
     *
     * @return The founded Artist list.
     */
    fun findLike(filters: Map<String, String>, filter: String = "name", order: Sort = Sort.ASCENDING): List<Artist> {
        return findArtistsLike(
            filters,
            filter,
            order
        ).toList()
    }

    /**
     * This function delete all the artists founded with the given conditions.
     */
    fun deleteArtistsByConditions(conditions: Map<String, String>) {
        realm.executeTransaction {
            findArtists(conditions).deleteAllFromRealm()
        }
    }

    /**
     * This function delete a single artist with the given conditions.
     *
     * @return Return true if the artist was founded and deleted, false either.
     */
    fun deleteSingleArtistByConditions(conditions: Map<String, String>): Boolean {
        val result = findSingleArtist(conditions) ?: return false

        realm.executeTransaction {
            result.deleteFromRealm()
        }

        return true
    }

    /**
     * This function delete all the artists of the database.
     */
    fun deleteAllArtists() {
        realm.executeTransaction {
            findArtists(HashMap()).deleteAllFromRealm()
        }
    }

    /**
     * This function delete the given list of artists.
     */
    fun deleteArtistsList(artists: List<Artist>) {
        realm.executeTransaction {
            for ( artist in artists ) {
                artist.deleteFromRealm()
            }
        }
    }

    /**
     * This function delete the given playlist.
     */
    fun deleteArtist(artist: Artist) {
        deleteArtistsList(listOf(artist))
    }

    fun findArtistById(id: Int): Artist? {
        val query = realm.where<Artist>(Artist::class.java)
        query.equalTo("id", id)
        return query.findFirst()
    }
}