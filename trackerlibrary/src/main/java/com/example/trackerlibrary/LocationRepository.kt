package com.example.trackerlibrary

import android.location.Location
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import java.util.*


@RealmClass
open class LocationRepository : RealmObject() {

    var latitude : Double? = 0.0
    var longitude : Double? = 0.0
    var speed: Float? = 0.0f
    var altitude: Double? = 0.0
    var generateDate :Date? = null

}