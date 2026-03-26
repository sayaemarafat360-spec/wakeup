package com.wakeupwarrior.data.repository

import com.wakeupwarrior.data.local.dao.StatsDao
import com.wakeupwarrior.data.local.entity.UserStatsEntity
import com.wakeupwarrior.data.local.entity.toModel
import com.wakeupwarrior.data.model.UserStats
import com.wakeupwarrior.data.model.toEntity
import com.wakeupwarrior.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val statsDao: StatsDao
) : StatsRepository {
    
    override fun getStats(): Flow<UserStats?> {
        return statsDao.getStats().map { it?.toModel() }
    }
    
    override suspend fun getStatsOnce(): UserStats? {
        return statsDao.getStatsOnce()?.toModel()
    }
    
    override suspend fun initializeStats() {
        statsDao.initializeStats()
    }
    
    override suspend fun updateStreak(streak: Int) {
        statsDao.updateStreak(streak)
    }
    
    override suspend fun recordWakeUp() {
        statsDao.incrementTotalWakeUps()
        statsDao.incrementChallengesCompleted()
        
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)
        statsDao.updateLastWakeUpDate(today)
    }
    
    override suspend fun checkAndUpdateStreak() {
        val stats = statsDao.getStatsOnce() ?: return
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Calendar.getInstance().time)
        
        val lastWakeUp = stats.lastWakeUpDate
        if (lastWakeUp != null) {
            val lastCalendar = Calendar.getInstance().apply {
                time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(lastWakeUp)!!
            }
            val todayCalendar = Calendar.getInstance()
            
            // Check if it's a consecutive day
            lastCalendar.add(Calendar.DAY_OF_MONTH, 1)
            if (lastCalendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR)) {
                // Consecutive day, increment streak
                val newStreak = stats.currentStreak + 1
                statsDao.updateStreak(newStreak)
            } else if (lastWakeUp != today) {
                // Streak broken
                statsDao.resetStreak()
            }
        }
    }
    
    override suspend fun addCoins(amount: Int) {
        statsDao.addCoins(amount)
    }
    
    override suspend fun deductCoins(amount: Int) {
        statsDao.deductCoins(amount)
    }
    
    override suspend fun updatePremiumStatus(isPremium: Boolean, until: Long?) {
        statsDao.updatePremiumStatus(isPremium, until)
    }
    
    override suspend fun updateAverageWakeTime(time: Long) {
        statsDao.updateAverageWakeTime(time)
    }
}
