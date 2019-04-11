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
import android.os.Build
import android.widget.Button
import com.example.trackerlibrary.R
import com.example.trackerlibrary.ActionReceiver



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
    fun sendNotification(context: Context, tittleMessage: String, messageContent: String, notificationType: String) {

        val notificationIntent = Intent(context,Class.forName("com.example.karen.apptestlibary.MainActivity"))
        val pendingInten = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channeId,description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                var notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)


                val answerIntent = Intent(context, Class.forName("com.example.karen.apptestlibary.MainActivity"))
                //val pendingIntentYes = PendingIntent.getActivity(context, 1, answerIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                val intentAction = Intent(context, ActionReceiver::class.java)
                val broadcast =  PendingIntent.getBroadcast(context,1,intentAction,PendingIntent.FLAG_UPDATE_CURRENT)

                builder = Notification.Builder(context, channeId)
                        .setContentTitle(tittleMessage)
                        .setContentText(messageContent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                        .setContentIntent(pendingInten)
                        .addAction(R.drawable.notification_icon_background, "http://google.com", broadcast)


                notificationManager!!.notify(1234, builder.build())
            }
            else
            {
                builder = Notification.Builder(context)
                        .setContentTitle(tittleMessage)
                        .setContentText(messageContent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                        .setContentIntent(pendingInten)
                var notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)
                notificationManager!!.notify(1234, builder.build())
            }



    }

    fun openLink() {

    }
}