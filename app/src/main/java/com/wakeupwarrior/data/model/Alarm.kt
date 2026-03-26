package com.wakeupwarrior.data.model

data class Alarm(
    val id: Long = 0,
    val label: String,
    val hour: Int,
    val minute: Int,
    val repeatDays: List<Int> = emptyList(),
    val isEnabled: Boolean = true,
    val challengeType: ChallengeType = ChallengeType.MATH,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val soundUri: String? = null,
    val vibrationEnabled: Boolean = true,
    val escalationEnabled: Boolean = true,
    val snoozeLimit: Int = 3,
    val snoozesUsed: Int = 0,
    val qrCodeValue: String? = null,
    val voicePhrase: String? = null,
    val shakeCount: Int = 20,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val displayTime: String
        get() = String.format("%02d:%02d", hour, minute)
    
    val isRepeating: Boolean
        get() = repeatDays.isNotEmpty()
    
    val repeatDaysText: String
        get() = if (repeatDays.isEmpty()) {
            "One-time"
        } else if (repeatDays.size == 7) {
            "Every day"
        } else if (repeatDays == listOf(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY)) {
            "Weekdays"
        } else if (repeatDays == listOf(Calendar.SATURDAY, Calendar.SUNDAY)) {
            "Weekends"
        } else {
            repeatDays.sorted().joinToString(", ") { it.toDayName() }
        }
}

fun Alarm.toEntity() = com.wakeupwarrior.data.local.entity.AlarmEntity(
    id = id,
    label = label,
    hour = hour,
    minute = minute,
    repeatDays = repeatDays.toString(),
    isEnabled = isEnabled,
    challengeType = challengeType.name,
    difficulty = difficulty.name,
    soundUri = soundUri,
    vibrationEnabled = vibrationEnabled,
    escalationEnabled = escalationEnabled,
    snoozeLimit = snoozeLimit,
    snoozesUsed = snoozesUsed,
    qrCodeValue = qrCodeValue,
    voicePhrase = voicePhrase,
    shakeCount = shakeCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun Int.toDayName(): String {
    return when (this) {
        Calendar.SUNDAY -> "Sun"
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        else -> ""
    }
}

// Need to import Calendar
import java.util.Calendar
