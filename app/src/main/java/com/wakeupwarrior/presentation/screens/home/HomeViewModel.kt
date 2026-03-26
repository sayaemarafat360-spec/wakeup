package com.wakeupwarrior.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.data.model.Alarm
import com.wakeupwarrior.data.model.UserStats
import com.wakeupwarrior.domain.usecase.alarm.DeleteAlarmUseCase
import com.wakeupwarrior.domain.usecase.alarm.GetAlarmsUseCase
import com.wakeupwarrior.domain.usecase.alarm.ToggleAlarmUseCase
import com.wakeupwarrior.domain.usecase.stats.GetStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getAlarmsUseCase: GetAlarmsUseCase,
    getStatsUseCase: GetStatsUseCase,
    private val toggleAlarmUseCase: ToggleAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase
) : ViewModel() {
    
    val alarms = getAlarmsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    
    val stats = getStatsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    fun toggleAlarm(alarmId: Long, enabled: Boolean) {
        viewModelScope.launch {
            toggleAlarmUseCase(alarmId, enabled)
        }
    }
    
    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            deleteAlarmUseCase(alarm)
        }
    }
}
