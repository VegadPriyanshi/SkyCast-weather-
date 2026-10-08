package com.pv.skycastweather.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

object LocationHelper {

    fun hasLocationPermission(activity: Activity): Boolean {

        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        activity: Activity,
        onLocationReceived: (Double, Double) -> Unit,
        onLocationFailed: () -> Unit
    ) {

        if (!hasLocationPermission(activity)) {
            onLocationFailed()
            return
        }

        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)

        val cancellationTokenSource =
            CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->

            if (location != null) {

                onLocationReceived(
                    location.latitude,
                    location.longitude
                )

            } else {

                onLocationFailed()

            }

        }.addOnFailureListener {

            onLocationFailed()

        }

    }

}