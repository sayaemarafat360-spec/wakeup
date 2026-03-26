package com.wakeupwarrior.data.model

enum class Difficulty(
    val displayName: String,
    val multiplier: Float,
    val coinsReward: Int
) {
    EASY(
        displayName = "Easy",
        multiplier = 0.5f,
        coinsReward = 5
    ),
    MEDIUM(
        displayName = "Medium",
        multiplier = 1.0f,
        coinsReward = 10
    ),
    HARD(
        displayName = "Hard",
        multiplier = 1.5f,
        coinsReward = 20
    ),
    NIGHTMARE(
        displayName = "Nightmare",
        multiplier = 2.0f,
        coinsReward = 50
    )
}
