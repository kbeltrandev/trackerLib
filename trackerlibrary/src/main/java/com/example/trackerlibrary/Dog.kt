package com.example.trackerlibrary

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass
open class Dog : RealmObject() {


   var name: String? = null
   var age: Int = 0

}