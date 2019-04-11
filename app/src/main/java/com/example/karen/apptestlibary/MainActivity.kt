package com.example.karen.apptestlibary



import android.accounts.AuthenticatorDescription
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.Button
import com.example.trackerlibrary.Tracker


class MainActivity : AppCompatActivity(){

    lateinit var btnMainSendSimpleNotification : Button
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    var channeId = "com.example.karen.apptestlibary"
    var description = "com.example.karen.apptestlibary"
    lateinit var context : Context

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTrackerLibrary()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingInten = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channeId,description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                var notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channeId)
                        .setContentTitle("CodeAndroid")
                        .setContentText("test notification")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
                        .setContentIntent(pendingInten)

                notificationManager!!.notify(1234, builder.build())
            }
            else
                {
                    builder = Notification.Builder(this)
                            .setContentTitle("CodeAndroid")
                            .setContentText("test notification")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
                            .setContentIntent(pendingInten)
                    var notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(notificationChannel)
                    notificationManager!!.notify(1234, builder.build())
                }

        }*/

    }

    private  fun initTrackerLibrary() {
        if (!Tracker.hasPermissions(this)) {
            Tracker.startLocationPermissionRequest(this)
            return
        }
        Tracker.getCustomParameters(this)
        Tracker.initTrackerService(this)
    }

  /*  private  fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val deviceToken = instanceIdResult.token
            Tracker.sendDeviceToken(deviceToken, this)
        }
    }*/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when {
            (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED -> {
                Tracker.initTrackerService(this)
            }
        }
    }
}
