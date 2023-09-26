package com.pp.ringqr

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel


class RecService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob()+ Dispatchers.IO)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RingQRApp::MyWakelockTag")
        wakeLock.acquire()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop( )
        }
        return super.onStartCommand(intent, flags, startId)
//        return Service.START_NOT_STICKY
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    private fun start(){
//        val notification=NotificationCompat.Builder(this, "rec_channel")
//            .setContentTitle("")
//            .setContentText("")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setOngoing(true )

//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//            startForeground(1, notification.build(),FOREGROUND_SERVICE_TYPE_MICROPHONE)
//        else
//            startForeground(1, notification.build())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "rec_channel",
                "bkp Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

//            val notificationBuilder = NotificationCompat.Builder(this, "rec_channel")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("Bkp")
//                .setContentText("")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                startForeground(1, notificationBuilder.build(), FOREGROUND_SERVICE_TYPE_MICROPHONE)
//            }
//            else
//                startForeground(1, notificationBuilder.build())
        }
    }
    private fun stop (){
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}