package com.wakeupwarrior.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wakeupwarrior.data.local.dao.AchievementDao
import com.wakeupwarrior.data.local.dao.AlarmDao
import com.wakeupwarrior.data.local.dao.ChallengeRecordDao
import com.wakeupwarrior.data.local.dao.StatsDao
import com.wakeupwarrior.data.local.entity.AchievementEntity
import com.wakeupwarrior.data.local.entity.AlarmEntity
import com.wakeupwarrior.data.local.entity.ChallengeRecordEntity
import com.wakeupwarrior.data.local.entity.UserStatsEntity

@Database(
    entities = [
        AlarmEntity::class,
        UserStatsEntity::class,
        AchievementEntity::class,
        ChallengeRecordEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun statsDao(): StatsDao
    abstract fun achievementDao(): AchievementDao
    abstract fun challengeRecordDao(): ChallengeRecordDao
}
