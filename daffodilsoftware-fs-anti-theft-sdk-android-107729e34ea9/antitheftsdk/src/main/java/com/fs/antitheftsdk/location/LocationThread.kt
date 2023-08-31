package com.fs.antitheftsdk.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.fs.antitheftsdk.background.UploadManager
import com.google.android.gms.location.*


/*This class is Location Thread to get lat lang of device and hit api */
internal class LocationThread(var ctx: Context) : Thread() {

    var mLocation: Location? = null


    override fun run() {
        super.run()
        Looper.prepare()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx)
            val mLocationRequestHighAccuracy = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

                if (location != null) {
                    UploadManager.getLocation(location, ctx)
                } else {

                    fusedLocationProviderClient.requestLocationUpdates(
                        mLocationRequestHighAccuracy,
                        object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                mLocation = locationResult.lastLocation

                                if (mLocation != null) {
                                    UploadManager.getLocation(mLocation!!, ctx)
                                } else {
                                    callToLocationService()
                                }
                            }

                            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                                val isLocationAvailable = locationAvailability.isLocationAvailable
                                if (!isLocationAvailable) {
                                    callToLocationService()
                                }
                            }

                        },
                        Looper.getMainLooper()
                    )

                }
            }

            fusedLocationProviderClient.lastLocation.addOnFailureListener {

                fusedLocationProviderClient.requestLocationUpdates(
                    mLocationRequestHighAccuracy,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            mLocation = locationResult.lastLocation

                            if (mLocation != null) {
                                UploadManager.getLocation(mLocation!!, ctx)
                            } else {
                                callToLocationService()
                            }
                        }
                    },
                    Looper.getMainLooper()
                )
            }

        } else {
            callToLocationService()
        }
    }

    private fun callToLocationService() {
        val i = Intent(ctx, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ctx.startForegroundService(i)
        }
        ctx.startService(i)
    }
}