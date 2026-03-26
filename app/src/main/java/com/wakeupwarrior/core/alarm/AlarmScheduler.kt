package com.wakeupwarrior.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.wakeupwarrior.core.util.Constants
import com.wakeupwarrior.core.util.calculateNextAlarmTime
import com.wakeupwarrior.data.model.Alarm
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    fun scheduleAlarm(alarm: Alarm) {
        if (!alarm.isEnabled) return
        
        val triggerTime = calculateNextAlarmTime(alarm.hour, alarm.minute, alarm.repeatDays)
        
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.Alarm.ACTION_TRIGGER
            putExtra(Constants.Alarm.EXTRA_ALARM_ID, alarm.id)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    // Fallback to inexact alarm
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
    
    fun cancelAlarm(alarmId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
    }
    
    fun rescheduleAllAlarms(alarms: List<Alarm>) {
        alarms.filter { it.isEnabled }.forEach { alarm ->
            scheduleAlarm(alarm)
        }
    }
    
    fun scheduleSnooze(alarmId: Long, delayMinutes: Long = 5) {
        val triggerTime = System.currentTimeMillis() + (delayMinutes * 60 * 1000)
        
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.Alarm.ACTION_TRIGGER
            putExtra(Constants.Alarm.EXTRA_ALARM_ID, alarmId)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (alarmId + 10000).toInt(), // Unique ID for snooze
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
