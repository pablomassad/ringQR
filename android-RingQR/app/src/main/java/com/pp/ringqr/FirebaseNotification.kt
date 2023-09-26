 package com.pp.ringqr

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.util.Date



 private lateinit var mediaPlayer: MediaPlayer

class FirebaseNotification:FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
        Log.v("PPP FCM", "onCreate")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val cmd = message.data["cmd"]
        val args =message.data["args"]
        Log.v("PPP cmd:", cmd.toString())

        if (cmd == "gps"){
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            val locationTask = fusedLocationProviderClient.lastLocation

            locationTask.addOnSuccessListener { location ->
                val timestamp: Timestamp = Timestamp.now()
                val dateFB: Date = timestamp.toDate()
                val time: Long = dateFB.time

                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude

                    data class GPSCoord(val lat: Number, val lng: Number, val time: Number)

                    val item = GPSCoord(lat, lng, time)

                    val app = applicationContext as RingQRApp
                    val user = app.topicname
                    Log.v("PPP user:", user)

                    val db = FirebaseFirestore.getInstance()
                    val documentReference = db.collection("userTracks").document(user)
                    documentReference.set(item)
                        .addOnSuccessListener {
                            Log.v("PPP firestore OK:", "$lat,$lng,$time")
                        }
                        .addOnFailureListener { e ->
                            Log.v("PPP firestore ERROR:", e.message.toString())
                        }
                }
                else{
                    Log.v("PPP location", "NO LOC detected")
                }
            }
            locationTask.addOnFailureListener { exception ->
                Log.v("PPP gps", "ERROR")
            }
        }
        if (cmd == "ring"){
            val argsObj = JSONObject(args)
            val hora = argsObj.getString("hora")
            var nombre = argsObj.getString("nombre")
            if (nombre == "")
                nombre = "NADIE"


            val channelId = "running_channel"
            val channelName = "Ring Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("IntegralMente")
                .setContentText("$nombre (TIMBRE a las $hora)")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.v("PPP Application", "onCreate")

                val channel = NotificationChannel(channelId, channelName, importance)
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val notificationManager = NotificationManagerCompat.from(applicationContext)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("PPP No notification permission", "POST_NOTIFICATIONS")
                return
            }
            notificationManager.notify(1, notificationBuilder.build())

            mediaPlayer = MediaPlayer.create(this, R.raw.ring)
            mediaPlayer.start()
        }
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.v("PPP onNewToken", token)
    }
    override fun onDestroy() {
        Log.v("PPP FirebaseNotification", "onDestroy")
        super.onDestroy()
        mediaPlayer.release() // Liberar recursos cuando la actividad se destruye
    }

    private fun beep(vol: Float){
        // mediaPlayer = MediaPlayer.create(this, R.raw.msg)
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer.setVolume(vol,vol)
        mediaPlayer.start()
    }


    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }
}
