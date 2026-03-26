package com.wakeupwarrior.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.wakeupwarrior.core.util.Constants
import com.wakeupwarrior.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepCounterService : Service(), SensorEventListener {
    
    private var sensorManager: SensorManager? = null
    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null
    
    private var stepsCount = 0
    
    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(Constants.Notification.ALARM_NOTIFICATION_ID + 1, createNotification())
        registerSensors()
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, Constants.Notification.CHANNEL_ID)
            .setContentTitle("Counting Steps")
            .setContentText("Step challenge in progress...")
            .setSmallIcon(android.R.drawable.ic_menu_directions)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
    
    private fun registerSensors() {
        stepDetectorSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        
        stepCounterSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (event.sensor.type) {
                Sensor.TYPE_STEP_DETECTOR -> {
                    stepsCount++
                    // Broadcast step count
                    val intent = Intent("com.wakeupwarrior.STEP_UPDATE").apply {
                        putExtra("steps", stepsCount)
                    }
                    sendBroadcast(intent)
                }
                Sensor.TYPE_STEP_COUNTER -> {
                    // This gives total steps since boot
                    // Use for reference
                }
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed
    }
    
    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}
