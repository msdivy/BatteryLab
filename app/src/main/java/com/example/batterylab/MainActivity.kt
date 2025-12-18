package com.example.batterylab

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var tvBatteryLevel: TextView
    private lateinit var sbBrightness: SeekBar
    private lateinit var etDuration: EditText
    private lateinit var btnStartTimer: Button
    private lateinit var tvTimerStatus: TextView
    private lateinit var tvStartBattery: TextView
    private lateinit var tvEndBattery: TextView
    private lateinit var btnClose: Button
    private lateinit var btnScreenshot: Button
    private lateinit var tvBrightnessPercentage: TextView

    private var currentBatteryLevel: Int = -1
    private var countDownTimer: CountDownTimer? = null

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                if (level != -1 && scale != -1) {
                    currentBatteryLevel = (level * 100 / scale.toFloat()).toInt()
                    tvBatteryLevel.text = getString(R.string.battery_label) + "$currentBatteryLevel%"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBatteryLevel = findViewById(R.id.tvBatteryLevel)
        sbBrightness = findViewById(R.id.sbBrightness)
        etDuration = findViewById(R.id.etDuration)
        btnStartTimer = findViewById(R.id.btnStartTimer)
        tvTimerStatus = findViewById(R.id.tvTimerStatus)
        tvStartBattery = findViewById(R.id.tvStartBattery)
        tvEndBattery = findViewById(R.id.tvEndBattery)
        btnClose = findViewById(R.id.btnClose)
        btnScreenshot = findViewById(R.id.btnScreenshot)
        tvBrightnessPercentage = findViewById(R.id.tvBrightnessPercentage)

        // Initialize Brightness Control
        val layoutParams = window.attributes
        sbBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val brightness = progress / 100f
                val lp = window.attributes
                lp.screenBrightness = brightness
                window.attributes = lp
                tvBrightnessPercentage.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val brightness = sbBrightness.progress / 100f
        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams

        // Initialize Timer
        btnStartTimer.setOnClickListener {
            startTimer()
        }

        btnClose.setOnClickListener {
            finish()
        }

        btnScreenshot.setOnClickListener {
            takeScreenshot()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryReceiver)
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        val durationString = etDuration.text.toString()
        val durationSeconds = durationString.toLongOrNull() ?: 0L

        if (durationSeconds <= 0) {
            tvTimerStatus.text = "Please enter valid duration"
            return
        }

        sbBrightness.isEnabled = false
        etDuration.isEnabled = false
        btnStartTimer.isEnabled = false
        btnClose.isEnabled = false

        tvTimerStatus.text = "Timer Started"

        tvStartBattery.text = "Battery at Start: $currentBatteryLevel%"
        tvEndBattery.text = getString(R.string.end_battery)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val durationMillis = durationSeconds * 1000

        countDownTimer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                tvTimerStatus.text = "Time Remaining: ${secondsRemaining}s"
            }

            override fun onFinish() {
                tvTimerStatus.text = "Timer Finished"
                tvEndBattery.text = "Battery at End: $currentBatteryLevel%"
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                btnClose.isEnabled = true
                sbBrightness.isEnabled = true
                //etDuration.isEnabled = true
                //btnStartTimer.isEnabled = true

            }
        }.start()
    }

    private fun takeScreenshot() {
        val now = Date()
        val filename = "Screenshot_${android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)}.jpg"

        val rootView = window.decorView.rootView
        val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        rootView.draw(canvas)

        var fos: OutputStream? = null
        var imageUri: Uri? = null

        try {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
            }

            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }

            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                fos.flush()
                Toast.makeText(this, "Screenshot saved to Gallery", Toast.LENGTH_LONG).show()
            } else {
                throw Exception("Failed to get output stream.")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save screenshot.", Toast.LENGTH_SHORT).show()
        } finally {
            fos?.close()
        }
    }
}
