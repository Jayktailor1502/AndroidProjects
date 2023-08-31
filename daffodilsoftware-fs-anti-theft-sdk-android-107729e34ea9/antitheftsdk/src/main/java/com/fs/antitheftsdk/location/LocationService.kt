package com.fs.antitheftsdk.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import com.fs.antitheftsdk.R
import com.fs.antitheftsdk.background.UploadManager

/*This class is location sevice to get location update*/
internal class LocationService : Service() {

    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        getLocationUpdates()
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun getLocationUpdates() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var locationGPS: Location? =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if (locationGPS != null) {
            UploadManager.getLocation(locationGPS, this)
            stopService(Intent(this, LocationService::class.java))
        } else {
            locationGPS = locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
            if (locationGPS != null) {
                UploadManager.getLocation(locationGPS, this)
                stopService(Intent(this, LocationService::class.java))

            } else {
                locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (locationGPS != null) {
                    UploadManager.getLocation(locationGPS, this)
                    stopService(Intent(this, LocationService::class.java))
                } else {
                    val handler = Handler(Looper.getMainLooper())
                    handler.post(Runnable {
                        Toast.makeText(this, getString(R.string.no_location), Toast.LENGTH_LONG)
                            .show()
                    })
                }
            }
        }
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted = false

    }

    companion object {
        var isServiceStarted = false
    }

}