package com.wakeupwarrior.domain.usecase.achievement

import com.wakeupwarrior.data.model.Achievement
import com.wakeupwarrior.data.model.AchievementDefinitions
import com.wakeupwarrior.domain.repository.AchievementRepository
import com.wakeupwarrior.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAchievementsUseCase @Inject constructor(
    private val repository: AchievementRepository
) {
    operator fun invoke(): Flow<List<Achievement>> = repository.getAllAchievements()
}

class InitializeAchievementsUseCase @Inject constructor(
    private val repository: AchievementRepository
) {
    suspend operator fun invoke() {
        repository.initializeAchievements()
    }
}

class UpdateAchievementProgressUseCase @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val statsRepository: StatsRepository
) {
    suspend operator fun invoke(achievementId: String, progress: Int) {
        val existing = achievementRepository.getAchievementById(achievementId)
        
        // Only update if not already unlocked
        if (existing?.isUnlocked != true) {
            achievementRepository.updateProgress(achievementId, progress)
            
            // Check if just unlocked
            val updated = achievementRepository.getAchievementById(achievementId)
            if (updated?.isUnlocked == true && existing?.isUnlocked != true) {
                // Award coins
                val definition = AchievementDefinitions.all.find { it.id == achievementId }
                definition?.coinsReward?.let { coins ->
                    statsRepository.addCoins(coins)
                }
            }
        }
    }
}

class CheckAndAwardAchievementsUseCase @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val statsRepository: StatsRepository
) {
    suspend operator fun invoke() {
        val stats = statsRepository.getStatsOnce() ?: return
        
        // Check First Rise
        if (stats.totalWakeUps >= 1) {
            updateProgress("first_rise", 1)
        }
        
        // Check Week Warrior
        if (stats.currentStreak >= 7) {
            updateProgress("week_warrior", stats.currentStreak)
        }
        
        // Check Month Master
        if (stats.currentStreak >= 30) {
            updateProgress("month_master", stats.currentStreak)
        }
        
        // Check Coin Collector
        if (stats.coins >= 1000) {
            updateProgress("coin_collector", stats.coins)
        }
    }
    
    private suspend fun updateProgress(achievementId: String, progress: Int) {
        val existing = achievementRepository.getAchievementById(achievementId)
        if (existing?.isUnlocked != true) {
            achievementRepository.updateProgress(achievementId, progress)
            
            val updated = achievementRepository.getAchievementById(achievementId)
            if (updated?.isUnlocked == true && existing?.isUnlocked != true) {
                val definition = AchievementDefinitions.all.find { it.id == achievementId }
                definition?.coinsReward?.let { coins ->
                    statsRepository.addCoins(coins)
                }
            }
        }
    }
}

class MarkAchievementViewedUseCase @Inject constructor(
    private val repository: AchievementRepository
) {
    suspend operator fun invoke(achievementId: String) {
        repository.markAsViewed(achievementId)
    }
}
