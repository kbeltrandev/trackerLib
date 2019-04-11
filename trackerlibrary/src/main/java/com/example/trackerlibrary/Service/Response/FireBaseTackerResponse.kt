package com.example.trackerlibrary.Service.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FireBaseTackerResponse (val error : String? = null, val response :Response)