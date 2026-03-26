package com.wakeupwarrior.data.model

enum class ChallengeType(
    val displayName: String,
    val description: String,
    val isPremium: Boolean = false,
    val icon: String
) {
    MATH(
        displayName = "Math Puzzle",
        description = "Solve math problems to wake up",
        isPremium = false,
        icon = "calculator"
    ),
    SHAKE(
        displayName = "Shake It Off",
        description = "Shake your phone multiple times",
        isPremium = false,
        icon = "shake"
    ),
    MEMORY(
        displayName = "Memory Game",
        description = "Remember and repeat patterns",
        isPremium = true,
        icon = "brain"
    ),
    QR(
        displayName = "QR Code Scan",
        description = "Scan a QR code in another room",
        isPremium = true,
        icon = "qr_code"
    ),
    TYPING(
        displayName = "Typing Challenge",
        description = "Type motivational quotes",
        isPremium = true,
        icon = "keyboard"
    ),
    VOICE(
        displayName = "Voice Command",
        description = "Say a phrase out loud",
        isPremium = true,
        icon = "mic"
    ),
    STEPS(
        displayName = "Step Counter",
        description = "Walk a number of steps",
        isPremium = true,
        icon = "directions_walk"
    )
}
