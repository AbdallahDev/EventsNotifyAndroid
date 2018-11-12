package com.sarayrah.abdallah.eventsnotify.services

import android.util.Log
import android.util.Log.*
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.sarayrah.abdallah.eventsnotify.data.Data

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()

        val refreshedToken = FirebaseInstanceId.getInstance().token

        //call this fun to store device token to the server.
        sendRegistrationToServer(refreshedToken)
    }

    /*this fun sends device token to the server to store it in the db so the device can receive
    notifications*/
    private fun sendRegistrationToServer(refreshedToken: String?) {
        val url = "${Data.saveDeviceTokenUrl}$refreshedToken"
        val requestQueue = Volley.newRequestQueue(this)
        val stringQueue = StringRequest(Request.Method.GET, url, Response.Listener {
            Toast.makeText(this, "تم تسجيلك لتلقي تنبيهات نشاطات المجلس",
                    Toast.LENGTH_LONG).show()
            d("fcm", "DeviceTokenSaved = $refreshedToken")
        }, Response.ErrorListener { error ->
            d("fcm", "instanceIdError" + error.message)
        })
        requestQueue.add(stringQueue)
    }
}