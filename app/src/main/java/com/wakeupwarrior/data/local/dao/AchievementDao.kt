package com.wakeupwarrior.data.local.dao

import androidx.room.*
import com.wakeupwarrior.data.local.entity.AchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<AchievementEntity>>
    
    @Query("SELECT * FROM achievements WHERE achievementId = :id")
    suspend fun getAchievementById(id: String): AchievementEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAchievement(achievement: AchievementEntity)
    
    @Query("UPDATE achievements SET progress = :progress WHERE achievementId = :id")
    suspend fun updateProgress(id: String, progress: Int)
    
    @Query("UPDATE achievements SET unlockedAt = :timestamp, progress = :targetProgress WHERE achievementId = :id")
    suspend fun unlockAchievement(id: String, timestamp: Long, targetProgress: Int)
    
    @Query("UPDATE achievements SET isViewed = 1 WHERE achievementId = :id")
    suspend fun markAsViewed(id: String)
    
    @Query("SELECT COUNT(*) FROM achievements WHERE unlockedAt IS NOT NULL")
    suspend fun getUnlockedCount(): Int
    
    @Transaction
    suspend fun initializeAchievements(achievementIds: List<String>) {
        achievementIds.forEach { id ->
            if (getAchievementById(id) == null) {
                saveAchievement(AchievementEntity(achievementId = id))
            }
        }
    }
}
