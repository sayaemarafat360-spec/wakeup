package com.wakeupwarrior.data.local.dao

import androidx.room.*
import com.wakeupwarrior.data.local.entity.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {
    
    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getStats(): Flow<UserStatsEntity?>
    
    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getStatsOnce(): UserStatsEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStats(stats: UserStatsEntity)
    
    @Transaction
    suspend fun initializeStats() {
        if (getStatsOnce() == null) {
            saveStats(UserStatsEntity(id = 1))
        }
    }
    
    @Query("UPDATE user_stats SET currentStreak = :streak, longestStreak = MAX(longestStreak, :streak) WHERE id = 1")
    suspend fun updateStreak(streak: Int)
    
    @Query("UPDATE user_stats SET currentStreak = 0 WHERE id = 1")
    suspend fun resetStreak()
    
    @Query("UPDATE user_stats SET totalWakeUps = totalWakeUps + 1 WHERE id = 1")
    suspend fun incrementTotalWakeUps()
    
    @Query("UPDATE user_stats SET coins = coins + :amount WHERE id = 1")
    suspend fun addCoins(amount: Int)
    
    @Query("UPDATE user_stats SET coins = coins - :amount WHERE id = 1")
    suspend fun deductCoins(amount: Int)
    
    @Query("UPDATE user_stats SET isPremium = :isPremium, premiumUntil = :until WHERE id = 1")
    suspend fun updatePremiumStatus(isPremium: Boolean, until: Long?)
    
    @Query("UPDATE user_stats SET totalChallengesCompleted = totalChallengesCompleted + 1 WHERE id = 1")
    suspend fun incrementChallengesCompleted()
    
    @Query("UPDATE user_stats SET lastWakeUpDate = :date WHERE id = 1")
    suspend fun updateLastWakeUpDate(date: String)
    
    @Query("UPDATE user_stats SET averageWakeTime = :time WHERE id = 1")
    suspend fun updateAverageWakeTime(time: Long)
}
