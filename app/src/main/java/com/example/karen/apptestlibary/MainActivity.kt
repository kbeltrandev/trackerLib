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

       // var customParameters = ArrayList<CustomParameters>''()


        Tracker.getCustomParameters(this)
        Tracker.initTrackerService(this)
    }

    private  fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val deviceToken = instanceIdResult.token
            Tracker.sendDeviceToken(deviceToken, this)
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
