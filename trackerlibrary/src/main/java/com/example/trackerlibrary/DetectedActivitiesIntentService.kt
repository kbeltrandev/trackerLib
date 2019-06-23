package com.example.trackerlibrary

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

class DetectedActivitiesIntentService : IntentService(DetectedActivitiesIntentService::class.java.name) {

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        if (!ActivityRecognitionResult.hasResult(intent)) return
        val result = ActivityRecognitionResult.extractResult(intent) ?: return
        if (result.mostProbableActivity.type == DetectedActivity.UNKNOWN || result.mostProbableActivity.type == DetectedActivity.TILTING) return
        broadcastNewActivity(result.mostProbableActivity)
    }

    private fun broadcastNewActivity(activity: DetectedActivity) {
        val intent = Intent("activity_intent")
        intent.putExtra("type", activity.type)
        intent.putExtra("typeName", getActivityString(activity.type))
        sendBroadcast(intent)
    }

    private fun getActivityString(detectedActivityType: Int): String {
        return when (detectedActivityType) {
            DetectedActivity.ON_BICYCLE -> "bicycle"
            DetectedActivity.ON_FOOT -> "foot"
            DetectedActivity.RUNNING -> "running"
            DetectedActivity.STILL -> "still"
            DetectedActivity.TILTING -> "tilting"
            DetectedActivity.WALKING -> "walking"
            DetectedActivity.IN_VEHICLE -> "vehicle"
            else -> "unknown"
        }
    }

}