package com.example.trackerlibrary.Service

import com.example.trackerlibrary.Core.Settings
import com.example.trackerlibrary.Core.Urls
import com.example.trackerlibrary.GpsDataPayload
import com.example.trackerlibrary.Service.Response.FireBaseTackerResponse
import com.example.trackerlibrary.Service.Response.TrackerResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface TrackerApi {

    @POST("/app1/device1.json")
        fun sendGpsPayload(@Body payload : FireBasePayload): Call<FireBaseTackerResponse>

    /*@POST("/location")
    fun sendGpsPayload(@Body payload : GpsDataPayload): Call<TrackerResponse>*/

    companion object Factory {
        fun create(): TrackerApi {

            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.connectTimeout(Settings.ServiceRequestSecondsTimeout, TimeUnit.SECONDS)

            val retrofit = Retrofit.Builder()
                    .client(okHttpClient.build())
                    .baseUrl(Urls.FirebaseApi)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(TrackerApi::class.java)
        }
    }
}