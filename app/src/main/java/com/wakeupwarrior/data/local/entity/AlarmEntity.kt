package com.wakeupwarrior.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wakeupwarrior.data.model.ChallengeType
import com.wakeupwarrior.data.model.Difficulty

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val label: String,
    val hour: Int,
    val minute: Int,
    val repeatDays: String, // JSON array: [1,2,3,4,5] for Mon-Fri
    val isEnabled: Boolean = true,
    val challengeType: String = ChallengeType.MATH.name,
    val difficulty: String = Difficulty.MEDIUM.name,
    val soundUri: String? = null,
    val vibrationEnabled: Boolean = true,
    val escalationEnabled: Boolean = true,
    val snoozeLimit: Int = 3,
    val snoozesUsed: Int = 0,
    val qrCodeValue: String? = null, // For QR challenge
    val voicePhrase: String? = null, // For voice challenge
    val shakeCount: Int = 20, // For shake challenge
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

fun AlarmEntity.toModel() = com.wakeupwarrior.data.model.Alarm(
    id = id,
    label = label,
    hour = hour,
    minute = minute,
    repeatDays = parseRepeatDays(repeatDays),
    isEnabled = isEnabled,
    challengeType = ChallengeType.valueOf(challengeType),
    difficulty = Difficulty.valueOf(difficulty),
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

private fun parseRepeatDays(json: String): List<Int> {
    if (json.isBlank()) return emptyList()
    return json.removeSurrounding("[", "]")
        .split(",")
        .mapNotNull { it.trim().toIntOrNull() }
}
