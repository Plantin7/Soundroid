package fr.uge.soundroid.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.lang.AssertionError

open class Artist(
    @PrimaryKey
    var id: Int? = 0,

    @Required
    var name: String? = null): RealmObject() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Artist

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun initPrimaryKey(): Int {
        id = hashCode()
        if ( id != null ) {
            return id!!
        } else {
            throw AssertionError()
        }
    }
}