package com.knoxpo.handle_notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/** HandleNotificationPlugin */
class HandleNotificationPlugin : FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private val TAG = HandleNotificationPlugin::class.java.simpleName
    private val METHOD_CHANNEL = "flutter_plugin"
    private val EVENT_CHANNEL = "event_plugin"
    private val CHANNEL_ID = "$TAG.CHANNEL_ID"
    private val ACTION_NOTIFICATION = "$TAG.ACTION_NOTIFICATION"

    private var context: Context? = null
    private var activity: Activity? = null
    private var anyValue: Any? = null

    var methodChannel: MethodChannel? = null
    private var eventChannel: EventChannel? = null

    private var eventChannelSink: EventChannel.EventSink? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.e(TAG, "onAttachedToEngine")
        //activityIsInBackground = false
        context = flutterPluginBinding.applicationContext

        flutterPluginBinding.applicationContext.let {
            this.context = it
        }

        methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, METHOD_CHANNEL)
        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, EVENT_CHANNEL)
        eventChannel?.setStreamHandler(
                object : EventChannel.StreamHandler {
                    override fun onListen(arguments: Any?, event: EventChannel.EventSink?) {
                        Log.d(TAG, "on Listen")
                        eventChannelSink = event

                        val isFromHistory = (activity!!.intent!!.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0

                        if (activity?.intent?.action == ACTION_NOTIFICATION && !isFromHistory) {
                            event?.success("Hello World")
                        }
                    }

                    override fun onCancel(arguments: Any?) {
                        Log.e(TAG, "onCancel")
                    }
                }
        )
        methodChannel?.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        Log.e(TAG, "onDetachedFromEngine")
        //activityIsInBackground = true
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        Log.d(TAG, context?.toString() ?: "No Context")

        when (call.method) {

            "notification" -> {

                //context?.registerReceiver(this,null)
                val name = call.argument<String>("name")
                val message = call.argument<String>("message")
                val email = call.argument<String>("email")

                showNotification(
                        name = name!!,
                        email = email!!,
                        message = message!!
                )
                result.success("notification")
            }

            "openScreen" -> {

                val isFromHistory = (activity!!.intent!!.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0

                if (this.activity?.intent?.action == ACTION_NOTIFICATION && !isFromHistory) {
                    result.success(true)
                } else {
                    result.success(false)
                }
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun showNotification(name: String, message: String, email: String) {

        Log.e(TAG, context.toString())
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel =
                    NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager?.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(context)
                .setContentTitle(name)
                .setContentInfo(email)
                .setTicker(name)
                .setContentText(message)
                //.setStyle(NotificationCompat.MessagingStyle(name))
                .setStyle(NotificationCompat.InboxStyle())
                .addAction(0, "ACCEPT", null)
                .addAction(0, "REJECT", null)
                .setSmallIcon(android.R.drawable.star_big_on)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .setContentIntent(setPendingIntent())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID) // Channel ID
        }
        notificationManager?.notify(0, builder.build())
        this.onDetachedFromActivity()
    }

    private fun setPendingIntent(): PendingIntent {
        val intent = Intent(context!!, getActvityClass(context!!))
        intent.action = ACTION_NOTIFICATION

        return PendingIntent
                .getActivity(
                        context!!,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
    }

    private fun getActvityClass(context: Context): Class<*> {
        val packageName = context.packageName
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        val className = launchIntent?.component?.className
        return try {
            Class.forName(className!!)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun onDetachedFromActivity() {
        //context?.unregisterReceiver(notificationReceiver)
        //activityIsInBackground = true
    }

    override fun onReattachedToActivityForConfigChanges(p0: ActivityPluginBinding) {
        Log.e(TAG, "onReattach")
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {

        Log.d(TAG, "onAttachedToActivity")
        this.activity = binding.activity
        //activityIsInBackground = false

        //Log.e(TAG, "onAttachedToActivity ${binding.activity.intent.action}")l

        binding.addOnNewIntentListener {
            if (it.action == ACTION_NOTIFICATION) {
                Log.e(TAG, "${it.action}")
                it.putExtra("name", "Pratik")
                it.putExtra("email", "abc@gmail.com")

                try {
                    if (it.extras != null) {
                        val dataMap = mutableMapOf<String, Any>()

                        it.extras!!.keySet().map { key ->
                            dataMap[key] = it.extras?.get(key) as Any
                        }
                        eventChannelSink?.success(dataMap)
                    } else {
                        eventChannelSink?.success("No result to be set")
                    }
                } catch (e: java.lang.Exception) {
                    Log.e(TAG, "Error ", e)
                }
                true
            } else {
                false
            }
        }
    }

    override fun onDetachedFromActivityForConfigChanges() {
        Log.e(TAG, "onDetachedFromA" +
                "ctivityForConfigChanges")
    }
}
