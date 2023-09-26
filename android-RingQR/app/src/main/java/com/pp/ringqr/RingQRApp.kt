package com.pp.ringqr

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import java.security.AccessController

class RingQRApp:Application() {

    var topicname: String = "BR"
    var version: String = "1.01.02"

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.v("PPP RingQRApp", "onCreate")

            val channelId = "running_channel"
            val channelName = "Ring Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}