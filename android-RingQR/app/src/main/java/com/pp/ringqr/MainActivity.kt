package com.pp.ringqr

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = applicationContext as RingQRApp
        val topicname = app.topicname
        val version = app.version

        FirebaseApp.initializeApp(this)
        Firebase.messaging.subscribeToTopic(topicname)
        Log.v("PPP subscriptions....", topicname)

        setContentView(R.layout.activity_main)
        val textViewTopic = findViewById<TextView>(R.id.textTopic)
        textViewTopic.text = topicname
        val textViewVer = findViewById<TextView>(R.id.textVersion)
        textViewVer.text = version
    }

    override fun onPause() {
        super.onPause()
        Log.v("PPP MainAct....", "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("PPP MainAct....", "onDestroy")
    }
}


