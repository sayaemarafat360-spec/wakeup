package com.wakeupwarrior.domain.repository

import com.wakeupwarrior.data.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAllAlarms(): Flow<List<Alarm>>
    fun getEnabledAlarms(): Flow<List<Alarm>>
    suspend fun getAlarmById(id: Long): Alarm?
    suspend fun getEnabledAlarmsList(): List<Alarm>
    suspend fun createAlarm(alarm: Alarm): Long
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun deleteAlarmById(id: Long)
    suspend fun setAlarmEnabled(id: Long, enabled: Boolean)
    suspend fun incrementSnoozes(id: Long)
    suspend fun resetSnoozes(id: Long)
    suspend fun getAlarmCount(): Int
}
