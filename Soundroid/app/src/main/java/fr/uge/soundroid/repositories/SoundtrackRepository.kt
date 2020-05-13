package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.Soundtrack
import io.realm.Realm
import io.realm.RealmResults

object SoundtrackRepository {

    /* TODO : close instance after service utilisation */
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    init {
        Log.i(SoundtrackRepository::class.java.name,"Soundtrack service created")
    }

    /**
     * This function save the given soundtracks in the database.
     */
    fun saveSoundtrackList(elements: List<Soundtrack>) {
        realm.beginTransaction()
        for ( element in elements ) {
            realm.copyToRealmOrUpdate(element)
        }
        realm.commitTransaction()
    }

    /**
     * This function save the given soundtrack in the database.
     */
    fun saveSoundtrack(element: Soundtrack) {
        saveSoundtrackList(
            listOf(
                element
            )
        )
    }

    /**
     * This function execute the realm request with the given conditions.
     * It returns the RealmResults (A list that contains all the founded elements)
     *
     * @return RealmResults<Soundtrack> the query result.
     */
    private fun findSoundtracks(conditions: Map<String, String>): RealmResults<Soundtrack> {
        val query = realm.where<Soundtrack>(Soundtrack::class.java)

        conditions.forEach {
            query.equalTo(it.key, it.value)
        }

        return query.findAll()
    }


    /**
     * This function execute the realm request with the given conditions.
     * It returns the RealmResults (A list that contains all the founded elements)
     *
     * @return RealmResults<Soundtrack> the query result.
     */
    private fun findSoundtracksLike(conditions: Map<String, String>): RealmResults<Soundtrack> {
        val query = realm.where<Soundtrack>(Soundtrack::class.java)

        conditions.forEach {
            query.like(it.key, "*${it.value}*")
        }

        return query.findAll()
    }

    /**
     * This function return the soundtrack founded with the given conditions.
     * If more or less than one soundtrack was founded the function return null.
     *
     * @return The founded soundtrack.
     */
    fun findSingleSoundtrack(conditions: Map<String, String>): Soundtrack? {
        val results =
            findSoundtracks(
                conditions
            )
        if ( conditions.size != 1 ) return null;
        return results.first()
    }

    /**
     * This function return the founded soundtracks in the database with the given conditions.
     *
     * @return The founded soundtracks list.
     */
    fun findSoundtracksList(conditions: Map<String, String>): List<Soundtrack> {
        return findSoundtracks(
            conditions
        ).toList()
    }

    /**
     * This function return all the soundtracks of the database.
     *
     * @return The founded Soundtrack list.
     */
    fun findAll(): List<Soundtrack> {
        return findSoundtracks(HashMap()).toList()
    }


    /**
     * This function return the albums filtered by the given parameters.
     *
     * @return The founded Soundtrack list.
     */
    fun findLike(filters: Map<String, String>): List<Soundtrack> {
        return findSoundtracksLike(
            filters
        ).toList()
    }

    /**
     * This function delete all the soundtracks founded with the given conditions.
     */
    fun deleteSoundtracksByConditions(conditions: Map<String, String>) {
        realm.executeTransaction {
            findSoundtracks(conditions).deleteAllFromRealm()
        }
    }

    /**
     * This function given a single soundtrack with the given conditions.
     *
     * @return Return true if the soundtrack was founded and deleted, false either.
     */
    fun deleteSingleSoundtrackByConditions(conditions: Map<String, String>): Boolean {
        val result = findSingleSoundtrack(conditions) ?: return false

        realm.executeTransaction {
            result.deleteFromRealm()
        }

        return true
    }

    /**
     * This function delete all the soundtracks of the database.
     */
    fun deleteAllSoundtracks() {
        realm.executeTransaction {
            findSoundtracks(HashMap()).deleteAllFromRealm()
        }
    }

    /**
     * This function delete the given list of soundtracks.
     */
    fun deleteSoundtracksList(soundtracks: List<Soundtrack>) {
        realm.executeTransaction {
            for ( soundtrack in soundtracks ) {
                soundtrack.deleteFromRealm()
            }
        }
    }

    /**
     * This function delete the given soundtrack.
     */
    fun deleteSoundtrack(soundtrack: Soundtrack) {
        deleteSoundtracksList(listOf(soundtrack))
    }
}