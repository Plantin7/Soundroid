package fr.uge.soundroid.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MusicNotificationActionPlayerService : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.sendBroadcast(Intent("ACTION_SOUNDTRACK").putExtra("action", intent.action))
    }
}