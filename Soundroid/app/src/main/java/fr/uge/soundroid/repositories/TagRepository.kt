package fr.uge.soundroid.repositories

import android.util.Log
import fr.uge.soundroid.models.Tag
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

object TagRepository {

    /* TODO : close instance after service utilisation */
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    init {
        Log.i(TagRepository::class.java.name,"Tag service created")
    }

    /**
     * This function save the given Tags in the database.
     */
    fun saveTagList(elements: List<Tag>) {
        realm.beginTransaction()
        for ( element in elements ) {
            realm.copyToRealmOrUpdate(element)
        }
        realm.commitTransaction()
    }

    /**
     * This function save the given Tag in the database.
     */
    fun saveTag(element: Tag) {
        saveTagList(
            listOf(
                element
            )
        )
    }

    /**
     * This function execute the realm request with the given conditions.
     * It returns the RealmResults (A list that contains all the founded elements)
     *
     * @return RealmResults<Tag> the query result.
     */
    private fun findTags(conditions: Map<String, String>, sorted: String = "name", order: Boolean = true): RealmResults<Tag> {
        val query = realm.where<Tag>(Tag::class.java)

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
     * @return RealmResults<Tag> the query result.
     */
    private fun findTagsLike(conditions: Map<String, String>, filter: String = "name", order: Sort = Sort.ASCENDING): RealmResults<Tag> {
        val query = realm.where<Tag>(Tag::class.java)

        conditions.forEach {
            query.like(it.key, "*${it.value}*", Case.INSENSITIVE)
        }

        query.sort(filter, order)

        return query.findAll()
    }

    /**
     * This function return the Tag founded with the given conditions.
     * If more or less than one Tag was founded the function return null.
     *
     * @return The founded Tag.
     */
    fun findSingleTag(conditions: Map<String, String>): Tag? {
        val results =
            findTags(
                conditions
            )
        if ( conditions.size != 1 ) return null;
        return results.first()
    }

    /**
     * This function return the founded Tags in the database with the given conditions.
     *
     * @return The founded Tags list.
     */
    fun findTagsList(conditions: Map<String, String>): List<Tag> {
        return findTags(
            conditions
        ).toList()
    }

    /**
     * This function return all the Tags of the database.
     *
     * @return The founded Tag list.
     */
    fun findAll(): List<Tag> {
        return findTags(HashMap()).toList()
    }


    /**
     * This function return the albums filtered by the given parameters.
     *
     * @return The founded Tag list.
     */
    fun findLike(filters: Map<String, String>, filter: String = "name", order: Sort = Sort.ASCENDING): List<Tag> {
        return findTagsLike(
            filters,
            filter,
            order
        ).toList()
    }

    /**
     * This function delete all the Tags founded with the given conditions.
     */
    fun deleteTagsByConditions(conditions: Map<String, String>) {
        realm.executeTransaction {
            findTags(conditions).deleteAllFromRealm()
        }
    }

    /**
     * This function given a single Tag with the given conditions.
     *
     * @return Return true if the Tag was founded and deleted, false either.
     */
    fun deleteSingleTagByConditions(conditions: Map<String, String>): Boolean {
        val result = findSingleTag(conditions) ?: return false

        realm.executeTransaction {
            result.deleteFromRealm()
        }

        return true
    }

    /**
     * This function delete all the Tags of the database.
     */
    fun deleteAllTags() {
        realm.executeTransaction {
            findTags(HashMap()).deleteAllFromRealm()
        }
    }

    /**
     * This function delete the given list of Tags.
     */
    fun deleteTagsList(tags: List<Tag>) {
        realm.executeTransaction {
            for ( tag in tags ) {
                tag.deleteFromRealm()
            }
        }
    }

    /**
     * This function delete the given Tag.
     */
    fun deleteTag(tag: Tag) {
        deleteTagsList(listOf(tag))
    }

    fun findTagById(id: Int): Tag? {
        val query = realm.where<Tag>(Tag::class.java)
        query.equalTo("id", id)
        return query.findFirst()
    }


}