package fr.uge.soundroid.activities.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.AlbumSoundtrackListFragment
import fr.uge.soundroid.repositories.AlbumRepository
import java.lang.AssertionError

class AlbumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        val albumId = intent.getIntExtra("albumId", 0)
        if ( albumId == 0 ) {
            Log.e(AlbumActivity::class.java.name, "The is is the default one !")
        }
        val album = AlbumRepository.findAlbumById(albumId)
            ?: throw AssertionError("This element needs to exist in the database")

        val fragment = AlbumSoundtrackListFragment(album)

        supportFragmentManager.beginTransaction()
            .add(R.id.activity_album_container, fragment)
            .commit()
    }
}
