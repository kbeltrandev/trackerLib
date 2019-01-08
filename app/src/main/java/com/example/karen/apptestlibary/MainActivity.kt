package com.example.karen.apptestlibary

import android.Manifest
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.example.trackerlibrary.TrackerService
import java.util.*


class MainActivity : AppCompatActivity() {

    var uuid : String? = null

    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreference =  getSharedPreferences("trackerPreferences", Context.MODE_PRIVATE)
        if(uuid == null)
        {
            var editor = sharedPreference.edit()
            editor.putString("trackerAppUUID",UUID.randomUUID().toString())
            editor.commit()
        }
        uuid = sharedPreference.getString("trackerAppUUID",null)

        if (!hasPermissions()) {
            startLocationPermissionRequest()
            return
        }
        startTrackerService()
    }

    private fun startTrackerService() {
        val intent = Intent(this, TrackerService::class.java)
        intent.putExtra("trackerAppUUID",  uuid)
        startService(intent)
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
        when {
            (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED -> {
                startTrackerService()
            }
        }
    }
}
