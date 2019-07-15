package com.example.trackerlibrary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.example.trackerlibrary.Service.FireBasePayload
import com.example.trackerlibrary.Service.LogsApi
import com.example.trackerlibrary.Service.Response.FireBaseTackerResponse
import com.example.trackerlibrary.Service.TrackerApi
import io.fabric.sdk.android.Fabric
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import android.widget.Toast
import com.example.trackerlibrary.PushNotifications.PushGenerator
import io.realm.annotations.RealmModule
import android.R.id.edit
import android.content.*
import android.location.Location
import android.os.*
import io.realm.Realm
import io.realm.RealmConfiguration


class Tracker {


    companion object {

        private val context : Context? = null
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

        // A reference to the service used to get location updates.
        private var mService: LocationUpdatesService? = null

        // Tracks the bound state of the service.
        private var mBound = false

        fun initTrackerService(context : Context) {
            Fabric.with(context, Crashlytics())

            Realm.init(context)
            val mRealmConfiguration = RealmConfiguration.Builder()
                    .name("xigodb.realm")
                    .modules(MyModule())
                    .schemaVersion(1) // skip if you are not managing
                    .deleteRealmIfMigrationNeeded()
                    .build()

            Realm.getInstance(mRealmConfiguration)
            Realm.setDefaultConfiguration(mRealmConfiguration)



            val intent = Intent(context, TrackerService::class.java)
            context.startService(intent)


            val intenta = Intent(context, LocationUpdatesService::class.java)


            context.bindService((intenta), mServiceConnection,
                    Context.BIND_AUTO_CREATE)

            Handler().postDelayed({
                mService!!.requestLocationUpdates()//INICIA SERVICIO DE GEOLOCALIZACION
            }, 40000)



        }

        private val mServiceConnection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                val binder = service as LocationUpdatesService.LocalBinder
                mService = binder.getService()
                mBound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mService = null
                mBound = false
            }
        }
        /**
         * Receiver for broadcasts sent by [LocationUpdatesService].
         */
        private class MyReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                var location = intent.getParcelableExtra<Location>(LocationUpdatesService.EXTRA_LOCATION);
                if (location != null) {

                }
            }
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


        fun getMetaData(context: Context): Bundle {
            return context.packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData
        }


        @SuppressLint("MissingPermission", "NewApi")
       /* fun sendDeviceToken(deviceToken : String?, context: Context) {

            val telephonyManager = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val gpsDataPayload  = FireBasePayload()
            val call = TrackerApi.create().sendGpsPayload(gpsDataPayload)

            call.enqueue(object : Callback<FireBaseTackerResponse> {
                override fun onResponse(call: Call<FireBaseTackerResponse>, response: Response<FireBaseTackerResponse>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        Log.i("MENSAJE","200 ok post")
                    }
                }
                override fun onFailure(call: Call<FireBaseTackerResponse>, t: Throwable) {
                    sendLogException(t)
                }
            })
        }*/

        private fun sendLogException(t: Throwable) {
            val call = LogsApi.create().sendLog(t.message.toString())
            call.enqueue(object : Callback<LogPayload> {
                override fun onResponse(call: Call<LogPayload>, response: Response<LogPayload>) {
                    Log.i("LOG SENT","200 ok")
                }
                override fun onFailure(call: Call<LogPayload>, t: Throwable) {

                }
            })
        }

        @SuppressLint("NewApi")
        fun getCustomParameters(context: Context) {
            val metaDatas =  context.packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)


          /*  var BUNDLE_KEY_MAP_STATE = "mapData"
            val metaDatasBundle = metaDatas.getBundle(BUNDLE_KEY_MAP_STATE)

            for (key in metaDatas.keySet()) {
                val key = metaDatas.getString(key)
                val value = metaDatas.getString("tracker.Apikey")
                    if(key.contains(value))
                        {

                        }
               }*/

        }

    }


   /* @RealmModule( library = true , classes = arrayOf(Dog::class))
    private class customModule*/
}


// Create the module
@RealmModule(library = true, allClasses = true)
internal class MyModule
