package com.example.trackerlibrary

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.util.Log
import com.example.trackerlibrary.Background.ForegroundServicesNotification
import com.example.trackerlibrary.Core.Settings
import com.example.trackerlibrary.Service.TrackerApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import android.telephony.TelephonyManager
import com.example.trackerlibrary.Service.Response.FireBaseTackerResponse
import com.example.trackerlibrary.Service.FireBasePayload
import java.util.Locale.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.*
import com.example.trackerlibrary.PushNotifications.PushGenerator
import com.example.trackerlibrary.Service.LogsApi
import com.example.trackerlibrary.Service.Response.SimpleResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.realm.Realm
import com.google.gson.JsonObject
import com.google.gson.Gson
import org.json.JSONObject


class TrackerService : Service() {

    /**
     * Provides access to the Fused LocationBackground Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    // A reference to the service used to get location updates.
    private var mService: LocationUpdatesService? = null

    private var handler: Handler? = null
    private var runnable: Runnable? = null


    inner class TestServiceBinder : Binder() {
        val service: TrackerService
            get() = this@TrackerService
    }

    private val binder = TestServiceBinder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(ForegroundServicesNotification.getNotificationId(), ForegroundServicesNotification.getNotification(applicationContext))
        resume()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        runnable = Runnable {
            getLastLocation()
        }
        handler = Handler()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler!!.removeCallbacks(runnable)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        handler!!.removeCallbacks(runnable)
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        Log.i("MENSAJE","service running")
       /* val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastLocation == null) lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (lastLocation != null) {
            if(isNetworkAvailable()) {
                sendGpsMessage(lastLocation)
            }
        }*/

        sendGpsMessage()
        handler!!.removeCallbacks(runnable)
        resume()
    }


    @SuppressLint("MissingPermission", "NewApi")
    private fun sendGpsMessage() = try {


        val realm = Realm.getDefaultInstance()
        // Persist your data in a transaction
        realm.beginTransaction()
        //final Dog managedDog = realm.copyToRealm(dog); // Persist unmanaged objects
        val queueLocations = realm.where(LocationRepository::class.java).findAll()
        realm.commitTransaction()

        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val bundle = packageManager.getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA).metaData

        val jsonCustomProperties = JsonObject()
        jsonCustomProperties.addProperty("edad", "29")
        jsonCustomProperties.addProperty("telefono", "3015002772")
        jsonCustomProperties.addProperty("sexo", "F")

        val customProperties = Gson().toJson(jsonCustomProperties)

        val gpsDataPayload = FireBasePayload()

        gpsDataPayload.deviceid = telephonyManager.imei
        gpsDataPayload.appid = bundle.getString("tracker.Apikey")
        gpsDataPayload.latitude = queueLocations.get(0)!!.latitude
        gpsDataPayload.longitude = queueLocations.get(0)!!.longitude
        gpsDataPayload.device = Build.MANUFACTURER
        gpsDataPayload.deviceModel = Build.MODEL
        gpsDataPayload.networkOperator = telephonyManager.networkOperatorName
        gpsDataPayload.reg = queueLocations.get(0)!!.generateDate
        gpsDataPayload.customProperties = jsonCustomProperties
        gpsDataPayload.deviceType = "Android"



        val call = TrackerApi.create().sendGpsPayload(gpsDataPayload)
        call.enqueue(object : Callback<FireBaseTackerResponse> {
            override fun onResponse(call: Call<FireBaseTackerResponse>, response: Response<FireBaseTackerResponse>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Log.i("MENSAJE","200 ok post")
                    gpsDataPayload.idcampaign =  response.body()!!.response.id
                    val hasMessage =  response.body()!!.response.mostrarMensaje
                    if(hasMessage!!.toBoolean()) {
                        sendPushMessage(response.body()!!, gpsDataPayload)
                    }

                }
            }
            override fun onFailure(call: Call<FireBaseTackerResponse>, t: Throwable) {
                sendLogException(t)
            }
        })
    }
    catch (e : Exception){
        sendLogException(e)
    }



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

    private fun sendLogException(t: Exception) {
        val call = LogsApi.create().sendLog(t.message.toString())
        call.enqueue(object : Callback<LogPayload> {
            override fun onResponse(call: Call<LogPayload>, response: Response<LogPayload>) {
                Log.i("LOG SENT","200 ok")
            }
            override fun onFailure(call: Call<LogPayload>, t: Throwable) {

            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private  fun sendPushMessage( notificationData : FireBaseTackerResponse , gpsDataPayload : FireBasePayload) {
        PushGenerator().sendNotification(this, notificationData , gpsDataPayload)
    }


    private fun resume() {
        handler!!.postDelayed(runnable, Settings.ServiceFrequencyMiliseconds)
    }

}
