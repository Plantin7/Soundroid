package fr.uge.soundroid.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class HistoryEntry(
    @PrimaryKey
    var id: Int? = null,

    @Required
    var date: Date? = Date(),

    var soundtrack: Soundtrack? = null
) : RealmObject() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistoryEntry

        if (id != other.id) return false
        if (date != other.date) return false
        if (soundtrack != other.soundtrack) return false

        return true
    }

    override fun hashCode(): Int {
        return "${date.toString()} ${soundtrack?.title}".hashCode()
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