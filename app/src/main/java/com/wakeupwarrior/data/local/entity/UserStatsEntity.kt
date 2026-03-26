package com.wakeupwarrior.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wakeupwarrior.data.model.UserStats

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey
    val id: Long = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalWakeUps: Int = 0,
    val coins: Int = 0,
    val isPremium: Boolean = false,
    val premiumUntil: Long? = null,
    val totalChallengesCompleted: Int = 0,
    val averageWakeTime: Long = 0,
    val lastWakeUpDate: String? = null, // Format: yyyy-MM-dd
    val createdAt: Long = System.currentTimeMillis()
)

fun UserStatsEntity.toModel() = UserStats(
    currentStreak = currentStreak,
    longestStreak = longestStreak,
    totalWakeUps = totalWakeUps,
    coins = coins,
    isPremium = isPremium,
    premiumUntil = premiumUntil,
    totalChallengesCompleted = totalChallengesCompleted,
    averageWakeTime = averageWakeTime,
    lastWakeUpDate = lastWakeUpDate
)
