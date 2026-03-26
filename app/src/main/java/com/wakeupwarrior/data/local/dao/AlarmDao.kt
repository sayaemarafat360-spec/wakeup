package com.wakeupwarrior.data.local.dao

import androidx.room.*
import com.wakeupwarrior.data.local.entity.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    
    @Query("SELECT * FROM alarms ORDER BY hour, minute ASC")
    fun getAllAlarms(): Flow<List<AlarmEntity>>
    
    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: Long): AlarmEntity?
    
    @Query("SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY hour, minute ASC")
    fun getEnabledAlarms(): Flow<List<AlarmEntity>>
    
    @Query("SELECT * FROM alarms WHERE isEnabled = 1")
    suspend fun getEnabledAlarmsList(): List<AlarmEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity): Long
    
    @Update
    suspend fun updateAlarm(alarm: AlarmEntity)
    
    @Delete
    suspend fun deleteAlarm(alarm: AlarmEntity)
    
    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteAlarmById(id: Long)
    
    @Query("UPDATE alarms SET isEnabled = :enabled WHERE id = :id")
    suspend fun setAlarmEnabled(id: Long, enabled: Boolean)
    
    @Query("UPDATE alarms SET snoozesUsed = snoozesUsed + 1 WHERE id = :id")
    suspend fun incrementSnoozes(id: Long)
    
    @Query("UPDATE alarms SET snoozesUsed = 0 WHERE id = :id")
    suspend fun resetSnoozes(id: Long)
    
    @Query("SELECT COUNT(*) FROM alarms")
    suspend fun getAlarmCount(): Int
}
