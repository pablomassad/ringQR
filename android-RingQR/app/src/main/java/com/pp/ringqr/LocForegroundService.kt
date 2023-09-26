package com.pp.ringqr

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import com.google.android.gms.location.LocationRequest

class LocForegroundService : Service() {

    private lateinit var locationManager: LocationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    class MyLocationListener(
        private val locationListener: com.google.android.gms.location.LocationListener
    ) : android.location.LocationListener {
        override fun onLocationChanged(location: Location) {
            locationListener.onLocationChanged(location)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Iniciar LocationManager
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val locationListener = MyLocationListener(object : com.google.android.gms.location.LocationListener {
            override fun onLocationChanged(location: Location) {
                // Guardar la geolocalización en Firestore
                // ...
            }
        })
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        return START_STICKY
    }
}