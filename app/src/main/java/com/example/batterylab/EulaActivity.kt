package com.example.batterylab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EulaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eula)

        val btnAccept: Button = findViewById(R.id.btnAccept)
        val btnDecline: Button = findViewById(R.id.btnDecline)

        btnAccept.setOnClickListener {
            val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            prefs.edit().putBoolean("eula_accepted", true).apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnDecline.setOnClickListener {
            finishAffinity() // Closes the app
        }
    }
}
