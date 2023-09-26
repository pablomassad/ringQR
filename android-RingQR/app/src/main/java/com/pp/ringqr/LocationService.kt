package com.pp.ringqr

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import androidx.core.app.NotificationCompat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class LocationService: Service() {
    private val serviceScope = CoroutineScope(SupervisorJob()+ Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices. getFusedLocationProviderClient( applicationContext))
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop( )
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    private var latOrig:Double = 0.0
    private var lngOrig:Double = 0.0

    private fun start(){
        val notification=NotificationCompat.Builder(this, "running_channel")
            .setContentTitle("")
            .setContentText("")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true )

        locationClient.getLocationUpdates(5000L)
            .catch{e->e.printStackTrace()}
            .onEach { location ->
                val db = FirebaseFirestore.getInstance()

                val lat = location.latitude
                val lng = location.longitude

                val distance = calculateDistance(latOrig, lngOrig, lat, lng)
                Log.v("PPP latLng dist:", "$lat - $lng / $distance")
                if (distance > 2.0) {
                    latOrig = lat
                    lngOrig = lng
                    Log.v("PPP distance:", distance.toString())

                    val timestamp: Timestamp = Timestamp.now()
                    val dateFB: Date = timestamp.toDate()
                    val time: Long = dateFB.time

                    data class GPSCoord(val lat: Number, val lng: Number, val time: Number)

                    val item = GPSCoord(lat, lng, time)

                    val date = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("YYMMdd")
                    val trackDayId = date.format(formatter)

                    val app = applicationContext as RingQRApp
                    val user = app.topicname

                    db.collection("userTracks/$user/trackDates/$trackDayId/coords")
                        .document(time.toString())
                        .set(item)
                        .addOnSuccessListener {
                            Log.v("PPP db saved:", "$lat,$lng")
                        }
                        .addOnFailureListener { e ->
                            Log.v("PPP db ERROR:", e.message.toString())
                        }
                }
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }
    private fun stop (){
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val R = 6371000.0 // Earth radius in meters

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }
}