package com.wakeupwarrior.data.local.dao

import androidx.room.*
import com.wakeupwarrior.data.local.entity.ChallengeRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeRecordDao {
    
    @Query("SELECT * FROM challenge_records ORDER BY completedAt DESC")
    fun getAllRecords(): Flow<List<ChallengeRecordEntity>>
    
    @Query("SELECT * FROM challenge_records WHERE alarmId = :alarmId ORDER BY completedAt DESC")
    fun getRecordsForAlarm(alarmId: Long): Flow<List<ChallengeRecordEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: ChallengeRecordEntity)
    
    @Query("SELECT COUNT(*) FROM challenge_records WHERE challengeType = :type")
    suspend fun getCountForChallengeType(type: String): Int
    
    @Query("SELECT AVG(timeTakenSeconds) FROM challenge_records")
    suspend fun getAverageCompletionTime(): Double?
    
    @Query("DELETE FROM challenge_records WHERE completedAt < :timestamp")
    suspend fun deleteOldRecords(timestamp: Long)
}
