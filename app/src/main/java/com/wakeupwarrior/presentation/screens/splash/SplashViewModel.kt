package com.wakeupwarrior.presentation.screens.splash

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.core.util.Constants
import com.wakeupwarrior.domain.repository.AlarmRepository
import com.wakeupwarrior.domain.repository.StatsRepository
import com.wakeupwarrior.domain.usecase.achievement.InitializeAchievementsUseCase
import com.wakeupwarrior.domain.usecase.stats.InitializeStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val initializeStatsUseCase: InitializeStatsUseCase,
    private val initializeAchievementsUseCase: InitializeAchievementsUseCase,
    private val alarmRepository: AlarmRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    
    private val _isOnboardingCompleted = MutableStateFlow<Boolean?>(null)
    val isOnboardingCompleted = _isOnboardingCompleted.asStateFlow()
    
    init {
        viewModelScope.launch {
            // Check if onboarding is completed
            val completed = dataStore.data
                .map { preferences -> 
                    preferences[booleanPreferencesKey(Constants.Preferences.KEY_ONBOARDING_COMPLETED)] ?: false 
                }
                .first()
            
            _isOnboardingCompleted.value = completed
            
            // Initialize stats and achievements
            initializeStatsUseCase()
            initializeAchievementsUseCase()
            
            // Reschedule any existing alarms
            val alarms = alarmRepository.getEnabledAlarmsList()
            // TODO: Schedule alarms using AlarmScheduler
            
            _isLoading.value = false
        }
    }
}
