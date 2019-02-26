package com.example.karen.apptestlibary

import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

 class JobService : JobService() {

        override fun onStartJob(jobParameters: JobParameters): Boolean {
            Log.d(TAG, "Performing long running task in scheduled job")
            // TODO(developer): add long running task here.
            return false
        }

        override fun onStopJob(jobParameters: JobParameters): Boolean {
            return false
        }

        companion object {

            private val TAG = "MyJobService"
        }

    }
