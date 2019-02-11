package com.example.trackerlibrary.Service

import com.example.trackerlibrary.Core.Settings
import com.example.trackerlibrary.Core.Urls
import com.example.trackerlibrary.LogPayload
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface LogsApi  {

    @POST("/log")
    fun sendLog(@Body payload: String): Call<LogPayload>

    companion object Factory {
        fun create(): LogsApi {

            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.connectTimeout(Settings.ServiceRequestSecondsTimeout, TimeUnit.SECONDS)

            val retrofit = Retrofit.Builder()
                    .client(okHttpClient.build())
                    .baseUrl(Urls.LogsApi)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(LogsApi::class.java)
        }
    }

}
