package com.wakeupwarrior.data.model

data class UserStats(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalWakeUps: Int = 0,
    val coins: Int = 0,
    val isPremium: Boolean = false,
    val premiumUntil: Long? = null,
    val totalChallengesCompleted: Int = 0,
    val averageWakeTime: Long = 0,
    val lastWakeUpDate: String? = null
) {
    val hasValidPremium: Boolean
        get() = isPremium || (premiumUntil != null && premiumUntil > System.currentTimeMillis())
}

fun UserStats.toEntity() = com.wakeupwarrior.data.local.entity.UserStatsEntity(
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
