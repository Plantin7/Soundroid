package fr.uge.soundroid.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Artist(
    @PrimaryKey
    var id: Int? = null,

    @Required
    var name: String? = null): RealmObject() {
}