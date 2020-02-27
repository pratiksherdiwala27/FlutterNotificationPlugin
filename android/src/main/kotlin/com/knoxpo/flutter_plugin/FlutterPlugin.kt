package com.knoxpo.flutter_plugin

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
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FlutterPlugin */
public class FlutterPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private val TAG = com.knoxpo.flutter_plugin.FlutterPlugin::class.java.simpleName
    private val METHOD_CHANNEL = "flutter_plugin"
    private val CHANNEL_ID = "$TAG.CHANNEL_ID"
    private val ACTION_NOTIFICATION = "$TAG.ACTION_NOTIFICATION"

    private val EXTRA_NOTIFICATION = "$TAG.EXTRA_NOTIFICATION"

    private var context: Context? = null
    private var activity: Activity? = null

    private var openingAction: String? = null
    var methodChannel: MethodChannel? = null

    var activityIsInBackground = false

    private val notificationReceiver = object : NotificationReceiver() {
        override fun onNotificationReceived() {
            Log.d(TAG, "Notification received")
            methodChannel?.invokeMethod("onNotification", null)
        }
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.e(TAG, "onAttachedToEngine")
        activityIsInBackground = false
        context = flutterPluginBinding.applicationContext

        flutterPluginBinding.applicationContext.let {
            this.context = it
        }

        methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, METHOD_CHANNEL)
        methodChannel?.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        Log.e(TAG, "onDetachedFromEngine")
        activityIsInBackground = true
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {

        Log.d(TAG, context?.toString() ?: "No Context")

        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${Build.VERSION.RELEASE}")
            }
            "notification" -> {
                //context?.registerReceiver(this,null)
                val name = call.argument<String>("name")
                val message = call.argument<String>("message")
                val email = call.argument<String>("email")
                showNotification(
                        name = name!!,
                        email = email!!,
                        message = message!!,
                        result = result
                )
                result.success("notification")
            }
            "onNotification" -> {
                Log.e(TAG, "Hello from native")
            }
            "openScreen" -> {

                if (this.activity?.intent?.action == ACTION_NOTIFICATION) {
                    //methodChannel?.invokeMethod("onNotification", null)
                    result.success(true)
                } else {
                    result.success(false)
                }

                openingAction = null
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun showNotification(name: String, message: String, email: String, result: Result) {

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
                .setStyle(NotificationCompat.MessagingStyle(name))
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
        //intent.putExtra(EXTRA_NOTIFICATION, "new data")

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
        activityIsInBackground = true
    }

    override fun onReattachedToActivityForConfigChanges(p0: ActivityPluginBinding) {
        Log.e(TAG, "onReattach")
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {

        Log.d(TAG, "onAttachedToActivity")

        this.activity = binding.activity
        activityIsInBackground = false
        this.openingAction = binding.activity.intent.action
        //Log.e(TAG, "onAttachedToActivity ${binding.activity.intent.action}")

        binding.addOnNewIntentListener {
            if (it.action == ACTION_NOTIFICATION) {
                Log.e(TAG, "${it.action}")
                methodChannel?.invokeMethod("onNotification", mapOf("data" to "Welcome from Native"))
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
