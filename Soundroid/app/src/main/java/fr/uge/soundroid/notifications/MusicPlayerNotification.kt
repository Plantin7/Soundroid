package fr.uge.soundroid.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.media.app.NotificationCompat.MediaStyle
import fr.uge.soundroid.R
import fr.uge.soundroid.activities.others.PlayerActivity
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.services.MusicNotificationActionPlayerService


class MusicPlayerNotification {
    companion object {
        const val CHANNEL_ID = "channel_1"
        const val ACTION_PREVIOUS = "action_previous"
        const val ACTION_PLAY = "action_play"
        const val ACTION_NEXT = "action_next"

        private lateinit var notification:Notification

        fun createNotification(context: Context, soundtrack: Soundtrack, play_button:Int, position:Int, size:Int) {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val mediaSessionCompat = MediaSessionCompat(context, "tag")

            val intentPrevious = Intent(context, MusicNotificationActionPlayerService::class.java).setAction(ACTION_PREVIOUS)
            val pendingIntentPrevious = PendingIntent.getBroadcast(context, 0,intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT)
            val drawablePrevious = R.drawable.ic_skip_previous_white_50dp

            val intentPlay = Intent(context, MusicNotificationActionPlayerService::class.java).setAction(ACTION_PLAY)
            val pendingIntentPlay = PendingIntent.getBroadcast(context, 0,intentPlay, PendingIntent.FLAG_UPDATE_CURRENT)

            val intentNext = Intent(context, MusicNotificationActionPlayerService::class.java).setAction(ACTION_NEXT)
            val pendingIntentNext = PendingIntent.getBroadcast(context, 0,intentNext, PendingIntent.FLAG_UPDATE_CURRENT)
            val drawableNext = R.drawable.ic_skip_next_white_50dp

            val resultIntent = Intent(context, PlayerActivity::class.java).setAction(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val pendingIntent = PendingIntent.getActivity(context, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle(soundtrack.title)
                .setContentText(soundtrack.artist?.name)
                .setLargeIcon(soundtrack.album?.getBitmap(context))
                .setOnlyAlertOnce(true) //show notification for only first time
                .setShowWhen(false)
                .addAction(drawablePrevious, "Previous", pendingIntentPrevious)
                .addAction(play_button, "Play", pendingIntentPlay)
                .addAction(drawableNext, "Next", pendingIntentNext)
                .setStyle(MediaStyle().setShowActionsInCompactView(0, 1, 2).setMediaSession(mediaSessionCompat.sessionToken))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .build()

            notificationManagerCompat.notify(1, notification)

        }
    }
}