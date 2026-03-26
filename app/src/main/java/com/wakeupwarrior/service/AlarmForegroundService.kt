package com.wakeupwarrior.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.wakeupwarrior.AlarmActivity
import com.wakeupwarrior.MainActivity
import com.wakeupwarrior.R
import com.wakeupwarrior.core.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmForegroundService : Service() {
    
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var alarmId: Long = -1L
    
    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alarmId = intent?.getLongExtra(Constants.Alarm.EXTRA_ALARM_ID, -1L) ?: -1L
        
        if (alarmId != -1L) {
            startForeground(Constants.Notification.ALARM_NOTIFICATION_ID, createNotification())
            startAlarm()
            launchAlarmActivity()
        }
        
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotification(): Notification {
        val stopIntent = Intent(this, AlarmForegroundService::class.java).apply {
            action = Constants.Alarm.ACTION_DISMISS
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, Constants.Notification.CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_alarm_title, "Wake Up!"))
            .setContentText(getString(R.string.notification_alarm_text))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(createActivityPendingIntent(), true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
            .build()
    }
    
    private fun createActivityPendingIntent(): PendingIntent {
        val intent = AlarmActivity.createIntent(this, alarmId)
        return PendingIntent.getActivity(
            this,
            alarmId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun startAlarm() {
        // Start sound
        try {
            val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                )
                setDataSource(this@AlarmForegroundService, alarmUri)
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Start vibration
        startVibration()
    }
    
    private fun startVibration() {
        vibrator?.let { v ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val pattern = longArrayOf(0, 500, 500, 500, 500, 500, 1000)
                v.vibrate(
                    VibrationEffect.createWaveform(pattern, 0),
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                )
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(longArrayOf(0, 500, 500, 500, 500, 500, 1000), 0)
            }
        }
    }
    
    private fun launchAlarmActivity() {
        val intent = AlarmActivity.createIntent(this, alarmId).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }
    
    private fun stopAlarm() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
        
        vibrator?.cancel()
        
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }
}
