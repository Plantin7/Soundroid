package fr.uge.soundroid

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class Soundroid : Application() {

    override fun onCreate() {
        super.onCreate()

        // init realm (database orm) :
        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .name("soundroid.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(configuration)
    }

}