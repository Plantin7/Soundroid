package fr.uge.soundroid.activities.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import fr.uge.soundroid.R
import fr.uge.soundroid.repositories.SoundtrackRepository.findSingleSoundtrack
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // TODO FAIRE LA RECHERCHE PAR ID
        val soundtrackTitle = intent.getStringExtra("soundtrack")
        val condition = mapOf<String, String>("title" to soundtrackTitle)
        val soundtrack = findSingleSoundtrack(condition)
        if (soundtrack != null) {
            player_album_picture.setImageBitmap(soundtrack.album?.getBitmap(this))
            player_soundtrack_name.text = soundtrack.title
            player_artist_name.text = soundtrack.artist?.name
        }



        player_pause_button.setOnClickListener{
            Toast.makeText(this, "Play ", Toast.LENGTH_SHORT).show()
        }
    }
}
