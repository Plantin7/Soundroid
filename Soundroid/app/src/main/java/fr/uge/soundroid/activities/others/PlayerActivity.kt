package fr.uge.soundroid.activities.others

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.TagDialogFragment
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.notifications.MusicPlayerNotification
import fr.uge.soundroid.repositories.SoundtrackRepository
import fr.uge.soundroid.repositories.SoundtrackRepository.findSoundtrackById
import fr.uge.soundroid.services.MusicPlayerService
import fr.uge.soundroid.services.ClearNotificationMusicPlayerService
import fr.uge.soundroid.utils.WebsiteService
import kotlinx.android.synthetic.main.activity_player.*
import java.util.concurrent.TimeUnit


class PlayerActivity : AppCompatActivity(), Playable {

    private lateinit var handler: Handler
    private var soundtrack: Soundtrack? = null
    private var soundtrackId: Int = 0
    private lateinit var soundtrackList: List<Soundtrack>
    private var currentPosition: Int = -1
    private lateinit var musicPlayerService: MusicPlayerService
    private var counter = 0 // /!\ this counter is for changing music when the seek bar is finished
    var mBound = false

    private var notificationManager: NotificationManager? = null

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MusicPlayerService.LocalBinder
            musicPlayerService = binder.getService()
            musicPlayerService.setSong(soundtrack!!.path)
            player_seekBar.max = musicPlayerService.getDuration() / 1000
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            stopRefresh()
            mBound = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        handler = Handler()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        soundtrackId = intent.getIntExtra("soundtrackId", 0)
        currentPosition = intent.getIntExtra("position", 0)
        soundtrack = findSoundtrackById(soundtrackId)
        soundtrackList = SoundtrackRepository.findAll()
        updateActivityView(soundtrack)

        /* Send the HTTPRequest */
        val s = soundtrack
        if ( s != null ) {
            WebsiteService.sendSoundtrackInfos(s)
        }

        createChannel()
        registerReceiver(broadcastReceiver, IntentFilter("ACTION_SOUNDTRACK"))
        startService(Intent(baseContext, ClearNotificationMusicPlayerService::class.java))

        /** Play and Pause Music*/
        player_pause_button.setOnClickListener {
            if (mBound) {
                if (!musicPlayerService.isPlaying()) {
                    onPlaySoundtrack()
                } else {
                    onPauseSoundtrack()
                }
            }
        }

        /** Next Music */
        player_next_button.setOnClickListener {
            if (mBound) {
                onNextSoundtrack()
            }
        }

        /** Previous Music */
        player_previous_button.setOnClickListener {
            if (mBound) {
                onPreviousSoundtrack()
            }
        }

        /** Seek Bar Progress */
        player_seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress == seekBar?.max) {
                    stopRefresh()
                    player_next_button.performClick() // Change music
                    counter = 0
                    return
                }
                if (progress == seekBar?.max?.minus(1)) {
                    counter += 1
                }
                if (mBound && fromUser) {
                    musicPlayerService.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                musicPlayerService.seekTo(seekBar.progress * 1000)
            }
        })

        player_rating_bar.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            SoundtrackRepository.realm.executeTransaction {
                val note = rating / ratingBar.numStars
                soundtrack?.note = note
                it.copyToRealmOrUpdate(soundtrack!!)
            }
        }

        player_tag_button.setOnClickListener {
            if (mBound) {
                val dialog = TagDialogFragment.createTagDialogFragment(soundtrackId)
                dialog.show(supportFragmentManager, "TagDialog")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            MusicPlayerNotification.CHANNEL_ID,
            "channel", NotificationManager.IMPORTANCE_LOW
        )
        notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (mBound) {
                when (intent.extras!!.getString("action")) {
                    MusicPlayerNotification.ACTION_PREVIOUS -> onPreviousSoundtrack()
                    MusicPlayerNotification.ACTION_PLAY ->
                        if (!musicPlayerService.isPlaying()) {
                            onPlaySoundtrack()
                        } else {
                            onPauseSoundtrack()
                        }
                    MusicPlayerNotification.ACTION_NEXT -> onNextSoundtrack()
                }
            }
        }
    }

    private fun updateActivityView(soundtrack: Soundtrack?) {
        if (soundtrack != null) {
            player_album_picture.setImageBitmap(soundtrack.album?.getBitmap(this))
            player_soundtrack_name.text = soundtrack.title
            player_artist_name.text = soundtrack.artist?.name
            player_end_time_text.text = soundtrack.duration?.toLong()?.let { formatTime(it) }
            player_start_time_Text.text = formatTime(0)
            if (soundtrack.note == null) player_rating_bar.rating = 0F
            else player_rating_bar.rating = soundtrack.note!! * player_rating_bar.numStars

            if (mBound) {
                player_pause_button.setBackgroundResource(R.drawable.ic_pause_white_50dp)
                musicPlayerService.stopAndReset()
                musicPlayerService.setSong(soundtrack.path)
                musicPlayerService.playSong()
                player_seekBar.max = musicPlayerService.getDuration() / 1000
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
        //unbindService(connection)
        //stopRefresh()
        //mBound = false
        Log.d("Testy", "Stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRefresh()
        notificationManager?.cancelAll()
        unregisterReceiver(broadcastReceiver)
    }

    /** Code executed periodically to display and display the timer */
    private val updateSeekBarRunnable = Runnable {
        handler.postDelayed(getUpdateSeekBarRunnable(), 1000)
        val currentPosition = musicPlayerService.getCurrentPosition()
        if (mBound) {
            updateSeekBar(currentPosition)
        }
        if ((currentPosition / 1000) == player_seekBar?.max?.minus(1)) {
            counter += 1
            if (counter == 3) {
                stopRefresh()
                player_next_button.performClick() // Change music
                counter = 0
            }
        }
    }

    /** Required to allow recursive call from increment runnable  */
    private fun getUpdateSeekBarRunnable(): Runnable {
        return updateSeekBarRunnable
    }

    private fun stopRefresh() {
        handler.removeCallbacks(getUpdateSeekBarRunnable())
    }

    /** Update all age of entries*/
    private fun updateSeekBar(currentPosition: Int) {
        player_seekBar.progress = currentPosition / 1000
        player_start_time_Text.text = formatTime(currentPosition.toLong())
    }

    private fun formatTime(millis: Long): String {
        return String.format(
            "%01d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

    override fun onPreviousSoundtrack() {
        currentPosition = getPreviousPosition(currentPosition)
        val tmpSoundtrack = soundtrackList[currentPosition]
        soundtrackId = tmpSoundtrack.id!!
        soundtrack = tmpSoundtrack
        updateActivityView(soundtrack)
        MusicPlayerNotification.createNotification(
            this@PlayerActivity, soundtrack!!,
            R.drawable.ic_pause_white_50dp, currentPosition, soundtrackList.size - 1
        )
    }

    override fun onPlaySoundtrack() {
        musicPlayerService.playSong()
        player_pause_button.setBackgroundResource(R.drawable.ic_pause_white_50dp)
        updateSeekBarRunnable.run()
        MusicPlayerNotification.createNotification(
            this@PlayerActivity, soundtrack!!,
            R.drawable.ic_pause_white_50dp, currentPosition, soundtrackList.size - 1
        )
    }

    override fun onPauseSoundtrack() {
        musicPlayerService.pauseSong()
        player_pause_button.setBackgroundResource(R.drawable.ic_play_arrow_white_50dp)
        stopRefresh()
        MusicPlayerNotification.createNotification(
            this@PlayerActivity, soundtrack!!,
            R.drawable.ic_play_arrow_white_50dp, currentPosition, soundtrackList.size - 1
        )
    }

    override fun onNextSoundtrack() {
        currentPosition = getNextPosition(currentPosition)
        val tmpSoundtrack = soundtrackList[currentPosition]
        soundtrackId = tmpSoundtrack.id!!
        soundtrack = tmpSoundtrack
        updateActivityView(soundtrack)
        MusicPlayerNotification.createNotification(
            this@PlayerActivity, soundtrack!!,
            R.drawable.ic_pause_white_50dp, currentPosition, soundtrackList.size - 1
        )
    }

    private fun getPreviousPosition(position: Int): Int {
        if (position - 1 < 0) {
            return soundtrackList.size - 1
        }
        return position - 1
    }

    private fun getNextPosition(position: Int): Int {
        if (position + 1 > soundtrackList.size - 1) {
            return 0
        }
        return position + 1
    }
}
