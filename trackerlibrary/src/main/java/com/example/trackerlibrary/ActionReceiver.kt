package com.example.trackerlibrary

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.net.Uri
import android.util.Log
import com.example.trackerlibrary.Service.FireBasePayload
import com.example.trackerlibrary.Service.Response.FireBaseTackerResponse
import com.example.trackerlibrary.Service.Response.SimpleResponse
import com.example.trackerlibrary.Service.TrackerApi
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();

        val url = intent.getStringExtra("urlContent")

        var tracker : FireBasePayload? = null


        val mapper = ObjectMapper()
        try {
            tracker = mapper.readValue<FireBasePayload>(intent.extras!!.getString("Tracker"), FireBasePayload::class.java!!)
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }

        val call = TrackerApi.create().sendCampaingAction(tracker!!)
        call.enqueue(object : Callback<SimpleResponse> {
            override fun onResponse(call: Call<SimpleResponse>, response: Response<SimpleResponse>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Log.i("MENSAJE","200 ok post")

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    context.startActivity(intent)

                    //This is used to close the notification tray
                    val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                    context.sendBroadcast(it)
                }
            }
            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                //Tracker.sendLogException(t) PENDIENTE HOMOLOGAR YEISON
            }
        })

    }

    fun performAction1() {

    }

    fun performAction2() {

    }

}