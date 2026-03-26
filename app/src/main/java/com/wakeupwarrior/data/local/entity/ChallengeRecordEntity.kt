package com.wakeupwarrior.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wakeupwarrior.data.model.ChallengeType

@Entity(tableName = "challenge_records")
data class ChallengeRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val alarmId: Long,
    val challengeType: String,
    val difficulty: String,
    val completedAt: Long = System.currentTimeMillis(),
    val timeTakenSeconds: Int,
    val wasSuccessful: Boolean = true
)
