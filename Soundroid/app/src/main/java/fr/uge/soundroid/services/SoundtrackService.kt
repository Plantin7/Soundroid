package fr.uge.soundroid.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import fr.uge.soundroid.models.Soundtrack
import io.realm.Realm

class SoundtrackService: Service() {

    lateinit var realm: Realm

    override fun onBind(intent: Intent?): IBinder? {

        realm = Realm.getDefaultInstance()
        Log.i("DEBUGGG", "HEY app !")

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        /* close realm database */
        realm.close()
    }

    fun saveSoundtrackList(soundtracks: List<Soundtrack>) {
        realm.beginTransaction()
        for ( soundtrack in soundtracks ) {
            realm.copyToRealmOrUpdate(soundtrack)
        }
        realm.commitTransaction()
    }

    fun saveSoundtrack(soundtrack: Soundtrack) {
        saveSoundtrackList(listOf(soundtrack))
    }

//    fun findSoundtrack(): Soundtrack? {
//
//    }
}