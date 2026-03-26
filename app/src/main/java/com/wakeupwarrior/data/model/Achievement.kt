package com.wakeupwarrior.data.model

data class Achievement(
    val id: String,
    val unlockedAt: Long? = null,
    val progress: Int = 0,
    val isViewed: Boolean = false
) {
    val isUnlocked: Boolean
        get() = unlockedAt != null
}

object AchievementDefinitions {
    val all = listOf(
        AchievementDefinition(
            id = "first_rise",
            title = "First Rise",
            description = "Complete your first alarm",
            icon = "star",
            targetProgress = 1,
            coinsReward = 50
        ),
        AchievementDefinition(
            id = "week_warrior",
            title = "Week Warrior",
            description = "Wake up 7 days in a row",
            icon = "fire",
            targetProgress = 7,
            coinsReward = 200
        ),
        AchievementDefinition(
            id = "month_master",
            title = "Month Master",
            description = "Wake up 30 days in a row",
            icon = "crown",
            targetProgress = 30,
            coinsReward = 1000
        ),
        AchievementDefinition(
            id = "early_bird",
            title = "Early Bird",
            description = "Wake up before 6 AM for a week",
            icon = "wb_sunny",
            targetProgress = 7,
            coinsReward = 300
        ),
        AchievementDefinition(
            id = "challenge_champion",
            title = "Challenge Champion",
            description = "Complete all challenge types",
            icon = "emoji_events",
            targetProgress = 7,
            coinsReward = 500
        ),
        AchievementDefinition(
            id = "no_snooze_hero",
            title = "No Snooze Hero",
            description = "Don't snooze for 30 days",
            icon = "block",
            targetProgress = 30,
            coinsReward = 0,
            specialReward = "Premium Week Free"
        ),
        AchievementDefinition(
            id = "math_genius",
            title = "Math Genius",
            description = "Solve 100 math challenges",
            icon = "calculate",
            targetProgress = 100,
            coinsReward = 500
        ),
        AchievementDefinition(
            id = "memory_king",
            title = "Memory King",
            description = "Complete 50 memory challenges",
            icon = "psychology",
            targetProgress = 50,
            coinsReward = 300
        ),
        AchievementDefinition(
            id = "shake_master",
            title = "Shake Master",
            description = "Shake your phone 500 times total",
            icon = "vibration",
            targetProgress = 500,
            coinsReward = 200
        ),
        AchievementDefinition(
            id = "night_owl",
            title = "Night Owl",
            description = "Wake up after midnight 10 times",
            icon = "nightlight",
            targetProgress = 10,
            coinsReward = 150
        ),
        AchievementDefinition(
            id = "perfect_timing",
            title = "Perfect Timing",
            description = "Wake up at exact alarm time 20 times",
            icon = "schedule",
            targetProgress = 20,
            coinsReward = 250
        ),
        AchievementDefinition(
            id = "coin_collector",
            title = "Coin Collector",
            description = "Collect 1000 coins total",
            icon = "monetization_on",
            targetProgress = 1000,
            coinsReward = 100
        )
    )
}

data class AchievementDefinition(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val targetProgress: Int,
    val coinsReward: Int,
    val specialReward: String? = null
)
