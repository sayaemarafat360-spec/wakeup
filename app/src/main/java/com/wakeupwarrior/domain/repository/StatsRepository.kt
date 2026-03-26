package com.wakeupwarrior.domain.repository

import com.wakeupwarrior.data.model.UserStats
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun getStats(): Flow<UserStats?>
    suspend fun getStatsOnce(): UserStats?
    suspend fun initializeStats()
    suspend fun updateStreak(streak: Int)
    suspend fun recordWakeUp()
    suspend fun checkAndUpdateStreak()
    suspend fun addCoins(amount: Int)
    suspend fun deductCoins(amount: Int)
    suspend fun updatePremiumStatus(isPremium: Boolean, until: Long?)
    suspend fun updateAverageWakeTime(time: Long)
}
