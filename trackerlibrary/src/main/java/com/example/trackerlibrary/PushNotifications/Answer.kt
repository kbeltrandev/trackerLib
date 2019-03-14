package com.example.trackerlibrary.PushNotifications

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.trackerlibrary.R

class Answer : AppCompatActivity() {

    private var tvAnswerReceiveText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_receive)

        tvAnswerReceiveText = findViewById(R.id.tvAnswerReceiveText) as TextView
        Log.d("Main", intent.action)

        tvAnswerReceiveText!!.text = intent.action
    }
}