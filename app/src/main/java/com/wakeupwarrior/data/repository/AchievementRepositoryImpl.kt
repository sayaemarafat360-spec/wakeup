package com.wakeupwarrior.data.repository

import com.wakeupwarrior.data.local.dao.AchievementDao
import com.wakeupwarrior.data.local.entity.AchievementEntity
import com.wakeupwarrior.data.local.entity.toModel
import com.wakeupwarrior.data.model.Achievement
import com.wakeupwarrior.data.model.AchievementDefinitions
import com.wakeupwarrior.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepositoryImpl @Inject constructor(
    private val achievementDao: AchievementDao
) : AchievementRepository {
    
    override fun getAllAchievements(): Flow<List<Achievement>> {
        return achievementDao.getAllAchievements().map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override suspend fun getAchievementById(id: String): Achievement? {
        return achievementDao.getAchievementById(id)?.toModel()
    }
    
    override suspend fun updateProgress(achievementId: String, progress: Int) {
        val existing = achievementDao.getAchievementById(achievementId)
        val definition = AchievementDefinitions.all.find { it.id == achievementId } ?: return
        
        if (existing?.unlockedAt == null) {
            if (progress >= definition.targetProgress) {
                achievementDao.unlockAchievement(
                    id = achievementId,
                    timestamp = System.currentTimeMillis(),
                    targetProgress = definition.targetProgress
                )
            } else {
                achievementDao.updateProgress(achievementId, progress)
            }
        }
    }
    
    override suspend fun unlockAchievement(achievementId: String) {
        val definition = AchievementDefinitions.all.find { it.id == achievementId } ?: return
        achievementDao.unlockAchievement(
            id = achievementId,
            timestamp = System.currentTimeMillis(),
            targetProgress = definition.targetProgress
        )
    }
    
    override suspend fun markAsViewed(achievementId: String) {
        achievementDao.markAsViewed(achievementId)
    }
    
    override suspend fun getUnlockedCount(): Int {
        return achievementDao.getUnlockedCount()
    }
    
    override suspend fun initializeAchievements() {
        achievementDao.initializeAchievements(AchievementDefinitions.all.map { it.id })
    }
}
