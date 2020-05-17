package fr.uge.soundroid.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import fr.uge.soundroid.models.Song

/**
 * @author Vincent_Agullo
 */
class MusicPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var songPath:String

    override fun onBind(intent: Intent?): IBinder? { return null }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "--SERVICE STARTED--", Toast.LENGTH_SHORT).show()
        stopSong()
        mediaPlayer = MediaPlayer()
        songPath = intent!!.getStringExtra("song")!!
        playSong()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun playSong() {
        mediaPlayer!!.setDataSource(applicationContext, Uri.parse(songPath))
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
    }

    private fun stopSong() {
        if(mediaPlayer != null){
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
    }
}