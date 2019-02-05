package com.example.trackerlibrary

data class GpsDataPayload (
        var latitude : Double? = 0.0,
        var longitude : Double? = 0.0,
        var speed: Float = 0.0f,
        var altitude: Double = 0.0,
        var batteryPercentage: Int = 0,
        var appVersion : String? = null,
        var geohash : String? = null,
        var device : String? = null,
        var osversion : String? = null,
        var networkOperator:  String? = null
)

