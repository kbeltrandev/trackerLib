package com.example.trackerlibrary

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.telephony.TelephonyManager
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.example.trackerlibrary.GeohasgGenerator.GeoHash
import com.example.trackerlibrary.Service.FireBasePayload
import com.example.trackerlibrary.Service.LogsApi
import com.example.trackerlibrary.Service.Response.FireBaseTackerResponse
import com.example.trackerlibrary.Service.TrackerApi
import io.fabric.sdk.android.Fabric
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*
import android.net.ConnectivityManager
import android.os.Bundle
import java.nio.BufferUnderflowException


class Tracker {


    companion object {

        private val context : Context? = null
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


        fun getMetaData(context: Context): Bundle {
            return context.packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData
        }


        @SuppressLint("MissingPermission", "NewApi")
        fun sendDeviceToken(deviceToken : String?, context: Context) {

            val telephonyManager = context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val gpsDataPayload  = FireBasePayload()
            gpsDataPayload.deviceToken = deviceToken

            val call = TrackerApi.create().sendGpsPayload(telephonyManager.imei, getMetaData(context).getString("tracker.Apikey"), gpsDataPayload)

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
}