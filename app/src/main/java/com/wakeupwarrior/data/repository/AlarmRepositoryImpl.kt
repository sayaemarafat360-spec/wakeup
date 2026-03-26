package com.wakeupwarrior.data.repository

import com.wakeupwarrior.data.local.dao.AlarmDao
import com.wakeupwarrior.data.local.entity.toModel
import com.wakeupwarrior.data.model.Alarm
import com.wakeupwarrior.data.model.toEntity
import com.wakeupwarrior.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao
) : AlarmRepository {
    
    override fun getAllAlarms(): Flow<List<Alarm>> {
        return alarmDao.getAllAlarms().map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override fun getEnabledAlarms(): Flow<List<Alarm>> {
        return alarmDao.getEnabledAlarms().map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override suspend fun getAlarmById(id: Long): Alarm? {
        return alarmDao.getAlarmById(id)?.toModel()
    }
    
    override suspend fun getEnabledAlarmsList(): List<Alarm> {
        return alarmDao.getEnabledAlarmsList().map { it.toModel() }
    }
    
    override suspend fun createAlarm(alarm: Alarm): Long {
        return alarmDao.insertAlarm(alarm.toEntity())
    }
    
    override suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm.toEntity())
    }
    
    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm.toEntity())
    }
    
    override suspend fun deleteAlarmById(id: Long) {
        alarmDao.deleteAlarmById(id)
    }
    
    override suspend fun setAlarmEnabled(id: Long, enabled: Boolean) {
        alarmDao.setAlarmEnabled(id, enabled)
    }
    
    override suspend fun incrementSnoozes(id: Long) {
        alarmDao.incrementSnoozes(id)
    }
    
    override suspend fun resetSnoozes(id: Long) {
        alarmDao.resetSnoozes(id)
    }
    
    override suspend fun getAlarmCount(): Int {
        return alarmDao.getAlarmCount()
    }
}
