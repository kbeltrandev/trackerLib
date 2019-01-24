package com.example.trackerlibrary

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.trackerlibrary.Background.ForegroundServicesNotification
import com.example.trackerlibrary.Core.Settings
import com.example.trackerlibrary.Service.Response.TrackerResponse
import com.example.trackerlibrary.Service.TrackerApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*
import android.telephony.TelephonyManager
import android.hardware.usb.UsbDevice.getDeviceId
import com.example.trackerlibrary.GeohasgGenerator.GeoHash
import com.example.trackerlibrary.Service.FireBasePayload
import com.example.trackerlibrary.Service.Response.FireBaseTackerResponse


class TrackerService : Service() {

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
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastLocation == null) lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (lastLocation != null) {
            sendGpsMessage(lastLocation)
        }
        handler!!.removeCallbacks(runnable)
        resume()
    }

    private fun sendGpsMessage(lastLocation : Location) {

        /*var geohash: GeoHash? = GeoHash.fromLocation(lastLocation)
        val gpsDataPayload  = GpsDataPayload()
        gpsDataPayload.lat = lastLocation.latitude
        gpsDataPayload.lon = lastLocation.longitude
        gpsDataPayload.uid = getDeviceImei()
        gpsDataPayload.geohash = geohash?.toString()

        val call = TrackerApi.create().sendGpsPayload(gpsDataPayload)
        call.enqueue(object : Callback<TrackerResponse> {
            override fun onResponse(call: Call<TrackerResponse>, response: Response<TrackerResponse>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Log.i("MENSAJE","200 ok")
                }
            }
            override fun onFailure(call: Call<TrackerResponse>, t: Throwable) {
            }
        })*/


        var geohash: GeoHash? = GeoHash.fromLocation(lastLocation)
        val gpsDataPayload  = FireBasePayload()
        gpsDataPayload.latitude = lastLocation.latitude
        gpsDataPayload.longitude = lastLocation.longitude
        gpsDataPayload.geohash = geohash?.toString()

        val call = TrackerApi.create().sendGpsPayload(gpsDataPayload)
        call.enqueue(object : Callback<FireBaseTackerResponse> {
            override fun onResponse(call: Call<FireBaseTackerResponse>, response: Response<FireBaseTackerResponse>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Log.i("MENSAJE","200 ok")
                }
            }
            override fun onFailure(call: Call<FireBaseTackerResponse>, t: Throwable) {
            }
        })

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceImei() : String? {
        val telephonyManager: TelephonyManager
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val deviceId = telephonyManager.deviceId
        return deviceId
    }

    private fun resume() {
        handler!!.postDelayed(runnable, Settings.ServiceFrequencyMiliseconds)
    }

}