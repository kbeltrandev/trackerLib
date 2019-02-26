package com.example.karen.apptestlibary


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import com.example.trackerlibrary.Tracker
import com.google.firebase.iid.FirebaseInstanceId


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTrackerLibrary()
        getDeviceToken()
    }

    private  fun initTrackerLibrary() {
        if (!Tracker.hasPermissions(this)) {
            Tracker.startLocationPermissionRequest(this)
            return
        }
        Tracker.initTrackerService(this)
    }

    private  fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val deviceToken = instanceIdResult.token
            // Do whatever you want with your token now
            // i.e. store it on SharedPreferences or DB
            // or directly send it to server
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when {
            (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED -> {
                Tracker.initTrackerService(this)
            }
        }
    }
}
