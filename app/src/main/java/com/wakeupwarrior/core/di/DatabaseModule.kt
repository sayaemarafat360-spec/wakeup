package com.wakeupwarrior.core.di

import com.wakeupwarrior.data.local.dao.AchievementDao
import com.wakeupwarrior.data.local.dao.AlarmDao
import com.wakeupwarrior.data.local.dao.ChallengeRecordDao
import com.wakeupwarrior.data.local.dao.StatsDao
import com.wakeupwarrior.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    fun provideAlarmDao(database: AppDatabase): AlarmDao {
        return database.alarmDao()
    }
    
    @Provides
    fun provideStatsDao(database: AppDatabase): StatsDao {
        return database.statsDao()
    }
    
    @Provides
    fun provideAchievementDao(database: AppDatabase): AchievementDao {
        return database.achievementDao()
    }
    
    @Provides
    fun provideChallengeRecordDao(database: AppDatabase): ChallengeRecordDao {
        return database.challengeRecordDao()
    }
}
