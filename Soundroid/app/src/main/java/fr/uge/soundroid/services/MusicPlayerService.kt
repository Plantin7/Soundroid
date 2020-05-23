package fr.uge.soundroid.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast


/**
 * @author Vincent_Agullo
 */
class MusicPlayerService : Service() {

    // Binder given to clients
    private val mBinder: IBinder = LocalBinder()

    private var mediaPlayer: MediaPlayer? = null

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }


    override fun onBind(intent: Intent?): IBinder? { return mBinder }

    override fun onCreate() {
        mediaPlayer = MediaPlayer()
        super.onCreate()
    }

    fun playSong() {
        mediaPlayer!!.start()
    }

    fun setSong(songPath:String?) {
        mediaPlayer!!.setDataSource(applicationContext, Uri.parse(songPath))
        mediaPlayer!!.prepare()
    }

    fun pauseSong() {
        if(mediaPlayer != null) {
            mediaPlayer!!.pause()
        }
    }

    fun stopAndReset() {
        if(mediaPlayer != null){
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
    }

    fun getCurrentPosition():Int {
        return mediaPlayer!!.currentPosition
    }

    fun getDuration():Int {
        return mediaPlayer!!.duration
    }

    fun isPlaying():Boolean {
        return mediaPlayer!!.isPlaying
    }
}