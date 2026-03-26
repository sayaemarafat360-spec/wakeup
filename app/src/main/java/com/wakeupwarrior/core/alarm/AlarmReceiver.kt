package com.wakeupwarrior.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.wakeupwarrior.core.util.Constants
import com.wakeupwarrior.service.AlarmForegroundService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var alarmScheduler: AlarmScheduler
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Constants.Alarm.ACTION_TRIGGER -> {
                val alarmId = intent.getLongExtra(Constants.Alarm.EXTRA_ALARM_ID, -1L)
                if (alarmId != -1L) {
                    startAlarmService(context, alarmId)
                }
            }
            
            Intent.ACTION_BOOT_COMPLETED,
            "android.intent.action.QUICKBOOT_POWERON",
            "com.htc.intent.action.QUICKBOOT_POWERON" -> {
                // Reschedule alarms after boot
                // This would be handled by a WorkManager job in production
            }
        }
    }
    
    private fun startAlarmService(context: Context, alarmId: Long) {
        val serviceIntent = Intent(context, AlarmForegroundService::class.java).apply {
            putExtra(Constants.Alarm.EXTRA_ALARM_ID, alarmId)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Start a work to reschedule alarms
            // In production, this would trigger a WorkManager job
        }
    }
}
