package com.knoxpo.action_notification

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** ActionNotificationPlugin */
public class ActionNotificationPlugin : FlutterPlugin, MethodCallHandler {

    private val TAG = ActionNotificationPlugin::class.java.simpleName
    private val METHOD_CHANNEL = "method_channel"
    private val EVENT_CHANNEL = "event_channel"
    private val ACTION_NOTIFICATION = "$TAG.ACTION_NOTIFICATION"

    private var context: Context? = null
    private var activity: Activity? = null

    private var methodChannel: MethodChannel? = null
    private var eventChannel: EventChannel? = null

    private var eventChannelSink: EventChannel.EventSink? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
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

    override fun onDetachedFromEngine(flutterBinding: FlutterPlugin.FlutterPluginBinding) { 
        Log.e(TAG,"onDetachedFromEngine")
    }

    override fun onMethodCall(call: MethodCall, result: Result) {

    }
}