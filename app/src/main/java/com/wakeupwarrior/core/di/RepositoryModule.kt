package com.wakeupwarrior.core.di

import com.wakeupwarrior.data.repository.AchievementRepositoryImpl
import com.wakeupwarrior.data.repository.AlarmRepositoryImpl
import com.wakeupwarrior.data.repository.StatsRepositoryImpl
import com.wakeupwarrior.domain.repository.AchievementRepository
import com.wakeupwarrior.domain.repository.AlarmRepository
import com.wakeupwarrior.domain.repository.StatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAlarmRepository(
        impl: AlarmRepositoryImpl
    ): AlarmRepository
    
    @Binds
    @Singleton
    abstract fun bindStatsRepository(
        impl: StatsRepositoryImpl
    ): StatsRepository
    
    @Binds
    @Singleton
    abstract fun bindAchievementRepository(
        impl: AchievementRepositoryImpl
    ): AchievementRepository
}
