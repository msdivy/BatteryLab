package com.example.batterylab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val eulaAccepted = prefs.getBoolean("eula_accepted", false)

        if (eulaAccepted) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, EulaActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}
