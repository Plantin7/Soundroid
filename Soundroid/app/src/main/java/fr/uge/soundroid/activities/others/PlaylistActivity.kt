package fr.uge.soundroid.activities.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.PlaylistSoundtrackListFragment
import fr.uge.soundroid.repositories.PlaylistRepository

class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val playlistId = intent.getIntExtra("playlistId", 0)
        if ( playlistId == 0 ) {
            Log.e(PlaylistActivity::class.java.name, "The is is the default one !")
        }

        val playlist = PlaylistRepository.findPlaylistById(playlistId)
            ?: throw AssertionError("This element needs to exist in the database")

        val fragment = PlaylistSoundtrackListFragment(playlist)
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_playlist_container, fragment)
            .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when( item.itemId ) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
