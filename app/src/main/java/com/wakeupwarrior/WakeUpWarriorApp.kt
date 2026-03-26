package com.wakeupwarrior

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.wakeupwarrior.core.util.Constants
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WakeUpWarriorApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarmChannel = NotificationChannel(
                Constants.Notification.CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.notification_channel_desc)
                enableVibration(true)
                enableLights(true)
            }

            val reminderChannel = NotificationChannel(
                Constants.Notification.REMINDER_CHANNEL_ID,
                "Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders for upcoming alarms"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannels(listOf(alarmChannel, reminderChannel))
        }
    }
}
