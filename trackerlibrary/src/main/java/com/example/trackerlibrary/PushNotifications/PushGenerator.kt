package com.example.trackerlibrary.PushNotifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.widget.Button
import com.example.trackerlibrary.R
import com.example.trackerlibrary.ActionReceiver
import com.example.trackerlibrary.Service.FireBasePayload
import com.example.trackerlibrary.Service.Response.FireBaseTackerResponse
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException


class PushGenerator {

    private var notificationTitle: String? = null
    private var notificationText: String? = null
    lateinit var btnMainSendSimpleNotification : Button
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    var channeId = "com.example.karen.apptestlibary"
    var description = "com.example.karen.apptestlibary"


    private fun setNotificationData() {
        notificationTitle = "Tracker Librari notificacion"
        notificationText = "Hello..This is a Notification Test"
    }

    @SuppressLint("NewApi")
    fun sendNotification(context: Context, notificationData : FireBaseTackerResponse , gpsDataPayload : FireBasePayload) {

        val notificationIntent = Intent(context,Class.forName("com.example.karen.apptestlibary.MainActivity"))
        val pendingInten = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { notificationChannel = NotificationChannel(channeId,description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                var notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)


                val answerIntent = Intent(context, Class.forName("com.example.karen.apptestlibary.MainActivity")) //Navega a la clase del proyecto deseado teniendo el contexto

                val intent = Intent(context, ActionReceiver::class.java)
                intent.putExtra("urlContent", notificationData.response.mensaje.btnOK!!.data)


                var jsonString = ""
                val mapper = ObjectMapper()
                try {
                    jsonString = mapper.writeValueAsString(gpsDataPayload)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                intent.putExtra("Tracker", jsonString)

                val broadcast =  PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                builder = Notification.Builder(context, channeId)
                        .setContentTitle(notificationData.response.mensaje.titulo)
                        .setContentText(notificationData.response.mensaje.mensaje)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                        .setContentIntent(pendingInten)
                        .addAction(R.drawable.notification_icon_background, notificationData.response.mensaje.btnOK.data, broadcast)



                notificationManager.notify(1234, builder.build())
            }
            else
            {
                val notificationIntent = Intent(context,Class.forName("com.example.karen.apptestlibary.MainActivity"))
                val pendingInten = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { notificationChannel = NotificationChannel(channeId,description, NotificationManager.IMPORTANCE_DEFAULT)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(true)
                    var notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(notificationChannel)


                    val answerIntent = Intent(context, Class.forName("com.example.karen.apptestlibary.MainActivity")) //Navega a la clase del proyecto deseado teniendo el contexto

                    val intent = Intent(context, ActionReceiver::class.java)
                    intent.putExtra("urlContent", notificationData.response.mensaje.btnOK!!.data)


                    var jsonString = ""
                    val mapper = ObjectMapper()
                    try {
                        jsonString = mapper.writeValueAsString(gpsDataPayload)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    intent.putExtra("Tracker", jsonString)

                    val broadcast =  PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                    builder = Notification.Builder(context, channeId)
                            .setContentTitle(notificationData.response.mensaje.titulo)
                            .setContentText(notificationData.response.mensaje.mensaje)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                            .setContentIntent(pendingInten)
                            .addAction(R.drawable.notification_icon_background, notificationData.response.mensaje.btnOK!!.data, broadcast)



                    notificationManager.notify(1234, builder.build())
            }
     }
   }

}