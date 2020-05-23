package fr.uge.soundroid.activities.others

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.SoundtrackRepository
import fr.uge.soundroid.repositories.SoundtrackRepository.findSoundtrackById
import fr.uge.soundroid.services.MusicPlayerService
import kotlinx.android.synthetic.main.activity_player.*
import java.util.concurrent.TimeUnit


class PlayerActivity : AppCompatActivity() {

    private lateinit var handler:Handler
    private var soundtrack:Soundtrack? = null
    private lateinit var soundtrackList:List<Soundtrack>
    private var currentPosition:Int = -1
    private lateinit var musicPlayerService: MusicPlayerService
    var mBound = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MusicPlayerService.LocalBinder
            musicPlayerService = binder.getService()
            musicPlayerService.setSong(soundtrack!!.path)
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        handler = Handler()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val soundtrackId = intent.getIntExtra("soundtrackId", 0)
        currentPosition = intent.getIntExtra("position", 0)
        soundtrack = findSoundtrackById(soundtrackId)
        soundtrackList = SoundtrackRepository.findAll()
        updateActivityView(soundtrack)

        player_pause_button.setOnClickListener{
            if(mBound) {
                player_seekBar.max = musicPlayerService.getDuration()
                if(!musicPlayerService.isPlaying()){
                    musicPlayerService.playSong()
                    player_pause_button.setBackgroundResource(R.drawable.ic_pause_white_50dp)
                    updateSeekBarRunnable.run()
                }
                else {
                    musicPlayerService.pauseSong()
                    player_pause_button.setBackgroundResource(R.drawable.ic_play_arrow_white_50dp)
                    stopRefresh()
                }
            }
        }

        player_next_button.setOnClickListener{
            if(mBound) {
                currentPosition = getNextPosition(currentPosition)
                soundtrack = soundtrackList[currentPosition]
                updateActivityView(soundtrack)
            }
        }

        player_previous_button.setOnClickListener{
            if(mBound) {
                currentPosition = getPreviousPosition(currentPosition)
                soundtrack = soundtrackList[currentPosition]
                updateActivityView(soundtrack)
            }
        }
    }

    private fun getPreviousPosition(position:Int):Int{
        if(position - 1 < 0){
            return soundtrackList.size - 1
        }
        return position - 1
    }

    private fun getNextPosition(position:Int):Int{
        if(position + 1 > soundtrackList.size - 1){
            return 0
        }
        return position + 1
    }

    private fun updateActivityView(soundtrack: Soundtrack?){
        if (soundtrack != null) {
            player_album_picture.setImageBitmap(soundtrack.album?.getBitmap(this))
            player_soundtrack_name.text = soundtrack.title
            player_artist_name.text = soundtrack.artist?.name
            player_end_time_text.text = soundtrack.duration?.toLong()?.let { formatTime(it) }
            player_start_time_Text.text = formatTime(0)
            if(mBound){
                player_pause_button.setBackgroundResource(R.drawable.ic_pause_white_50dp)
                musicPlayerService.stopAndReset()
                musicPlayerService.setSong(soundtrack.path)
                musicPlayerService.playSong()
                updateSeekBarRunnable.run()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, MusicPlayerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        stopRefresh()
        mBound = false
    }

    /** Code executed periodically to display and display the timer */
    private val updateSeekBarRunnable = Runnable {
        handler.postDelayed(getUpdateSeekBarRunnable(), 1000)
        updateSeekBar()
    }

    /** Required to allow recursive call from increment runnable  */
    private fun getUpdateSeekBarRunnable(): Runnable {
        return updateSeekBarRunnable
    }

    private fun stopRefresh() {
        handler.removeCallbacks(getUpdateSeekBarRunnable())
    }


    /** Update all age of entries*/
    private fun updateSeekBar() {
        val totalDuration = musicPlayerService.getDuration()
        val currentPosition = musicPlayerService.getCurrentPosition()
        if(mBound && currentPosition < totalDuration) {
            player_seekBar.progress = currentPosition
            player_start_time_Text.text = formatTime(currentPosition.toLong())
        }
    }

    private fun formatTime(millis: Long): String {
        return String.format(
            "%01d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        )
    }
}
