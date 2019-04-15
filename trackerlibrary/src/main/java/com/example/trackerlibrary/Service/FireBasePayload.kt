package com.example.trackerlibrary.Service

 data class FireBasePayload (
         var appid : String? = null,
         var deviceid : String? = null,
         var latitude : Double? = 0.0,
         var longitude : Double? = 0.0,
         var idcampaign : String? = null,
         var speed: Float = 0.0f,
         var altitude: Double = 0.0,
         var batteryPercentage: Int = 0,
         var appVersion : String? = null,
         var device : String? = null,
         var deviceModel : String? = null,
         var osVersion : String? = null,
         var networkOperator:  String? = null,
         var deviceLanguage : String? = null
 )