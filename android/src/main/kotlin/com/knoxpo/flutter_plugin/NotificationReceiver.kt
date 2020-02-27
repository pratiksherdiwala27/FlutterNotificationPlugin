package com.knoxpo.flutter_plugin

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private val TAG = NotificationReceiver::class.java.simpleName

abstract class NotificationReceiver : BroadcastReceiver() {

    companion object {

        const val ACTION_NOTIFICATION_CLICKED = "ACTION_NOTIFICATION_CLICKED"
        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(ACTION_NOTIFICATION_CLICKED)

            return PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "${intent?.action}")
        onNotificationReceived()
    }

    abstract fun onNotificationReceived()
}