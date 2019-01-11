package com.example.karen.apptestlibary


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import com.example.trackerlibrary.Tracker


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Tracker.hasPermissions(this)) {
            Tracker.startLocationPermissionRequest(this)
            return
        }
        Tracker.initTrackerService(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when {
            (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED -> {
                Tracker.initTrackerService(this)
            }
        }
    }
}
