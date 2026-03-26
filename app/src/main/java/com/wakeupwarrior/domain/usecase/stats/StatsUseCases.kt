package com.wakeupwarrior.domain.usecase.stats

import com.wakeupwarrior.core.util.Constants
import com.wakeupwarrior.data.model.UserStats
import com.wakeupwarrior.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    operator fun invoke(): Flow<UserStats?> = repository.getStats()
}

class InitializeStatsUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    suspend operator fun invoke() {
        repository.initializeStats()
    }
}

class RecordWakeUpUseCase @Inject constructor(
    private val statsRepository: StatsRepository
) {
    suspend operator fun invoke(): Int {
        statsRepository.checkAndUpdateStreak()
        statsRepository.recordWakeUp()
        
        // Return coins earned
        val stats = statsRepository.getStatsOnce()
        val streakBonus = (stats?.currentStreak ?: 0) * Constants.Gamification.COINS_STREAK_BONUS
        val totalCoins = Constants.Gamification.COINS_DAILY_BASE + streakBonus
        
        statsRepository.addCoins(totalCoins)
        
        return totalCoins
    }
}

class UpdateStreakUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    suspend operator fun invoke(streak: Int) {
        repository.updateStreak(streak)
    }
}

class AddCoinsUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    suspend operator fun invoke(amount: Int) {
        repository.addCoins(amount)
    }
}

class DeductCoinsUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    suspend operator fun invoke(amount: Int): Boolean {
        val stats = repository.getStatsOnce()
        return if ((stats?.coins ?: 0) >= amount) {
            repository.deductCoins(amount)
            true
        } else {
            false
        }
    }
}

class UpdatePremiumStatusUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    suspend operator fun invoke(isPremium: Boolean, until: Long? = null) {
        repository.updatePremiumStatus(isPremium, until)
    }
}

class CheckPremiumStatusUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    suspend operator fun invoke(): Boolean {
        val stats = repository.getStatsOnce() ?: return false
        return stats.hasValidPremium
    }
}
