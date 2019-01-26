package com.example.trackerlibrary.ServiceFireBase

import com.google.firebase.iid.FirebaseInstanceIdService

class FirebaseIntanceService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
    }

}