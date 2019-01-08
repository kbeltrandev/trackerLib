package com.example.karen.apptestlibary

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.example.trackerlibrary.TrackerService


class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!hasPermissions()) {
            startLocationPermissionRequest()
            return
        }
        startService(Intent(this, TrackerService::class.java))
    }

    private fun hasPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != REQUEST_PERMISSIONS_REQUEST_CODE) return
        when {
            (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED -> {
                startService(Intent(this, TrackerService::class.java))
            }
        }
    }
}
