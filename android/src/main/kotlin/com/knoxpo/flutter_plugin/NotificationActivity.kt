package com.knoxpo.flutter_plugin

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle

abstract class NotificationActivity : Activity(){

    companion object {

        val ACTION_NOTIFICATION = "ACTION_NOTIFICATION"

        fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(ACTION_NOTIFICATION)
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openActivity()
    }

    abstract fun openActivity()
}