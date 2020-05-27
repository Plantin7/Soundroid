package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.HistoryEntry
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

object HistoryEntryRepository {

    /* TODO : close instance after service utilisation */
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    init {
        Log.i(HistoryEntryRepository::class.java.name,"HistoryEntry service created")
    }

    /**
     * THis function save the given HistoryEntries in the database.
     */
    fun saveHistoryEntryList(elements: List<HistoryEntry>) {
        realm.beginTransaction()
        for ( element in elements ) {
            realm.copyToRealmOrUpdate(element)
        }
        realm.commitTransaction()
    }

    /**
     * This function save the given HistoryEntry in the database.
     */
    fun saveHistoryEntry(element: HistoryEntry) {
        saveHistoryEntryList(
            listOf(
                element
            )
        )
    }

    /**
     * This function execute the realm request with the given conditions.
     * It returns the RealmResults (A list that contains all the founded elements)
     *
     * @return RealmResults<HistoryEntry> the query result.
     */
    private fun findHistoryEntries(conditions: Map<String, String>, sorted: String = "date", order: Boolean = false): RealmResults<HistoryEntry> {
        val query = realm.where<HistoryEntry>(HistoryEntry::class.java)

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
     * @return RealmResults<HistoryEntry> the query result.
     */
    private fun findHistoryEntriesLike(conditions: Map<String, String>): RealmResults<HistoryEntry> {
        val query = realm.where<HistoryEntry>(HistoryEntry::class.java)

        conditions.forEach {
            query.like(it.key, "*${it.value}*", Case.INSENSITIVE)
        }

        return query.findAll()
    }

    /**
     * This function return the HistoryEntry founded with the given conditions.
     * If more or less than one HistoryEntry was founded the function return null.
     *
     * @return The founded HistoryEntry.
     */
    fun findSingleHistoryEntry(conditions: Map<String, String>): HistoryEntry? {
        val results =
            findHistoryEntries(
                conditions
            )
        if ( conditions.size != 1 ) return null;
        return results.first()
    }

    /**
     * This function return the founded HistoryEntries in the database with the given conditions.
     *
     * @return The founded HistoryEntries list.
     */
    fun findHistoryEntriesList(conditions: Map<String, String>): List<HistoryEntry> {
        return findHistoryEntries(
            conditions
        ).toList()
    }

    /**
     * This function return all the HistoryEntries of the database.
     *
     * @return The founded HistoryEntries list.
     */
    fun findAll(): List<HistoryEntry> {
        return findHistoryEntries(HashMap()).toList()
    }

    /**
     * This function return the albums filtered by the given parameters.
     *
     * @return The founded HistoryEntry list.
     */
    fun findLike(filters: Map<String, String>): List<HistoryEntry> {
        return findHistoryEntriesLike(
            filters
        ).toList()
    }

    /**
     * This function delete all the HistoryEntries founded with the given conditions.
     */
    fun deleteHistoryEntriesByConditions(conditions: Map<String, String>) {
        realm.executeTransaction {
            findHistoryEntries(conditions).deleteAllFromRealm()
        }
    }

    /**
     * This function return a single HistoryEntry with the given conditions.
     *
     * @return Return true if the HistoryEntry was founded and deleted, false either.
     */
    fun deleteSingleHistoryEntryByConditions(conditions: Map<String, String>): Boolean {
        val result = findSingleHistoryEntry(conditions) ?: return false

        realm.executeTransaction {
            result.deleteFromRealm()
        }

        return true
    }

    /**
     * This function delete all the HistoryEntries of the database.
     */
    fun deleteAllHistoryEntries() {
        realm.executeTransaction {
            findHistoryEntries(HashMap()).deleteAllFromRealm()
        }
    }

    /**
     * This function delete the given list of HistoryEntries.
     */
    fun deleteHistoryEntriesList(HistoryEntries: List<HistoryEntry>) {
        realm.executeTransaction {
            for ( HistoryEntry in HistoryEntries ) {
                HistoryEntry.deleteFromRealm()
            }
        }
    }

    /**
     * This function delete the given HistoryEntry.
     */
    fun deleteHistoryEntry(HistoryEntry: HistoryEntry) {
        deleteHistoryEntriesList(listOf(HistoryEntry))
    }

    fun findHistoryEntryById(id: Int): HistoryEntry? {
        val query = realm.where<HistoryEntry>(HistoryEntry::class.java)
        query.equalTo("id", id)
        return query.findFirst()
    }

}