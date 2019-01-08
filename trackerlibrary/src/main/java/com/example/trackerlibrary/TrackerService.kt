package com.example.trackerlibrary

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.trackerlibrary.Core.Settings
import com.example.trackerlibrary.Service.Response.TrackerResponse
import com.example.trackerlibrary.Service.TrackerApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class TrackerService : Service() {

    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private lateinit var uuid : UUID

    inner class TestServiceBinder : Binder() {
        val service: TrackerService
            get() = this@TrackerService
    }

    private val binder = TestServiceBinder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        resume()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        uuid = UUID.randomUUID()
        runnable = Runnable { testRunnable() }
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
    private fun testRunnable() {

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

        val gpsDataPayload  = GpsDataPayload()
        gpsDataPayload.lat = lastLocation.latitude
        gpsDataPayload.lon = lastLocation.longitude
        gpsDataPayload.uid = uuid.toString()

        val call = TrackerApi.create().sendGpsPayload(gpsDataPayload)
        call.enqueue(object : Callback<TrackerResponse> {
            override fun onResponse(call: Call<TrackerResponse>, response: Response<TrackerResponse>) {
                if (response?.code() == HttpURLConnection.HTTP_OK) {
                    Log.i("POST ON DB", "SERVICE REQUEST : 200")
                }
            }
            override fun onFailure(call: Call<TrackerResponse>, t: Throwable) {
            }
        })
    }

    private fun resume() {
        handler!!.postDelayed(runnable, Settings.ServiceFrequencyMiliseconds)
    }

}