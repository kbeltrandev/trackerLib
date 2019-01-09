package com.example.trackerlibrary.Background

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

object ForegroundServicesNotification {

    private const val NOTIFICATION_ID = 7737
    private const val CHANNEL_ID = "com.example.karen.apptestlibary.RUNNING_NOTIFICATION"
    private const val CHANNEL_NAME = "Auto Start Notification Channel"

    private var notification: Notification? = null

    fun getNotification(context: Context): Notification? {

        if (notification == null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
                channel.setSound(null, null)
                channel.setShowBadge(false)
                notificationManager!!.createNotificationChannel(channel)
                notification = Notification.Builder(context, CHANNEL_ID)
                        .setOngoing(true)
                        .setAutoCancel(false)
                        .build()
                return notification
            }

            val notificationIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            notificationIntent.data = Uri.parse("package:${context.applicationContext.packageName}")
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            notification = Notification.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setAutoCancel(false)
                    .setSound(null)
                    .setPriority(Notification.PRIORITY_LOW)
                    .setContentTitle("Tracker lib is running")
                    .setContentText("Touch for more information or to stop the app")
                    .setPriority(Notification.PRIORITY_MAX)
                    .build()
            return  notification

        }

        return notification
    }

    fun getNotificationId(): Int {
        return NOTIFICATION_ID
    }

}