package com.example.trackerlibrary

data class GpsDataPayload (
        var lat : Double? = 0.0,
        var lon : Double? = 0.0,
        var uid : String? = null,
        var geohash : String? = null
)