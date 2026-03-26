package com.wakeupwarrior.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wakeupwarrior.data.model.Achievement

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val achievementId: String,
    val unlockedAt: Long? = null,
    val progress: Int = 0,
    val isViewed: Boolean = false
)

fun AchievementEntity.toModel() = Achievement(
    id = achievementId,
    unlockedAt = unlockedAt,
    progress = progress,
    isViewed = isViewed
)
