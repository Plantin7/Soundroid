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
     * THis function save the given soundtracks in the database.
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
        Log.d("Testy", query.findAll().toString())

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
}