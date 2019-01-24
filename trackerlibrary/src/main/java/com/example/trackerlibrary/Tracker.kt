package com.example.trackerlibrary

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


class Tracker {

    companion object {

        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

        fun initTrackerService(context : Context) {
            Fabric.with(context, Crashlytics())
            val intent = Intent(context, TrackerService::class.java)
            context.startService(intent)
        }


        fun hasPermissions(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        }

        fun startLocationPermissionRequest(activity: Activity) {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE),
                    REQUEST_PERMISSIONS_REQUEST_CODE)
        }


    }

}