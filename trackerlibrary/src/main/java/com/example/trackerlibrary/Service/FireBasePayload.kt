package com.example.trackerlibrary.Service

import com.example.trackerlibrary.CustomParameters
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.ObjectInput
import java.util.*
import kotlin.collections.ArrayList

data class FireBasePayload (
        var appid: String? = null,
        var deviceid: String? = null,
        var deviceType: String? = null,
        var latitude: Double? = 0.0,
        var longitude: Double? = 0.0,
        var idcampaign: String? = null,
        var appVersion: String? = null,
        var device: String? = null,
        var deviceModel: String? = null,
        var networkOperator: String? = null,
        var deviceLanguage: String? = null,
        var reg: Date? = null,
        var customProperties: JsonObject? = null

 )