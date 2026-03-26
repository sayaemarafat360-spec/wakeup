package com.wakeupwarrior.domain.repository

import com.wakeupwarrior.data.model.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    fun getAllAchievements(): Flow<List<Achievement>>
    suspend fun getAchievementById(id: String): Achievement?
    suspend fun updateProgress(achievementId: String, progress: Int)
    suspend fun unlockAchievement(achievementId: String)
    suspend fun markAsViewed(achievementId: String)
    suspend fun getUnlockedCount(): Int
    suspend fun initializeAchievements()
}
