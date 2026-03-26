package com.wakeupwarrior.presentation.screens.alarm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.data.model.Alarm
import com.wakeupwarrior.data.model.ChallengeType
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.domain.repository.AlarmRepository
import com.wakeupwarrior.domain.usecase.alarm.CreateAlarmUseCase
import com.wakeupwarrior.domain.usecase.alarm.UpdateAlarmUseCase
import com.wakeupwarrior.domain.usecase.stats.CheckPremiumStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class CreateAlarmUiState(
    val hour: Int = 7,
    val minute: Int = 0,
    val label: String = "",
    val repeatDays: List<Int> = emptyList(),
    val challengeType: ChallengeType = ChallengeType.MATH,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val vibrationEnabled: Boolean = true,
    val escalationEnabled: Boolean = true,
    val snoozeLimit: Int = 3,
    val isLoading: Boolean = false
) {
    val canSave: Boolean
        get() = true // Always can save for now
}

@HiltViewModel
class CreateAlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val createAlarmUseCase: CreateAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val checkPremiumStatusUseCase: CheckPremiumStatusUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val alarmId: Long? = savedStateHandle.get<String>("alarmId")?.toLongOrNull()
    
    private val _uiState = MutableStateFlow(CreateAlarmUiState())
    val uiState = _uiState.asStateFlow()
    
    private val _isPremium = MutableStateFlow(false)
    val isPremium = _isPremium.asStateFlow()
    
    private val _saveResult = MutableStateFlow<Boolean?>(null)
    val saveResult = _saveResult.asStateFlow()
    
    init {
        viewModelScope.launch {
            _isPremium.value = checkPremiumStatusUseCase()
            
            // Load existing alarm if editing
            alarmId?.let { id ->
                alarmRepository.getAlarmById(id)?.let { alarm ->
                    _uiState.value = CreateAlarmUiState(
                        hour = alarm.hour,
                        minute = alarm.minute,
                        label = alarm.label,
                        repeatDays = alarm.repeatDays,
                        challengeType = alarm.challengeType,
                        difficulty = alarm.difficulty,
                        vibrationEnabled = alarm.vibrationEnabled,
                        escalationEnabled = alarm.escalationEnabled,
                        snoozeLimit = alarm.snoozeLimit
                    )
                }
            }
        }
    }
    
    fun updateTime(hour: Int, minute: Int) {
        _uiState.value = _uiState.value.copy(hour = hour, minute = minute)
    }
    
    fun updateLabel(label: String) {
        _uiState.value = _uiState.value.copy(label = label)
    }
    
    fun toggleDay(day: Int) {
        val currentDays = _uiState.value.repeatDays
        val newDays = if (currentDays.contains(day)) {
            currentDays - day
        } else {
            currentDays + day
        }
        _uiState.value = _uiState.value.copy(repeatDays = newDays)
    }
    
    fun updateChallengeType(type: ChallengeType) {
        if (type.isPremium && !_isPremium.value) {
            // Show premium prompt
            return
        }
        _uiState.value = _uiState.value.copy(challengeType = type)
    }
    
    fun updateDifficulty(difficulty: Difficulty) {
        _uiState.value = _uiState.value.copy(difficulty = difficulty)
    }
    
    fun updateVibration(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(vibrationEnabled = enabled)
    }
    
    fun updateEscalation(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(escalationEnabled = enabled)
    }
    
    fun updateSnoozeLimit(limit: Int) {
        _uiState.value = _uiState.value.copy(snoozeLimit = limit)
    }
    
    fun saveAlarm() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val state = _uiState.value
            val alarm = Alarm(
                id = alarmId ?: 0,
                label = state.label,
                hour = state.hour,
                minute = state.minute,
                repeatDays = state.repeatDays,
                isEnabled = true,
                challengeType = state.challengeType,
                difficulty = state.difficulty,
                vibrationEnabled = state.vibrationEnabled,
                escalationEnabled = state.escalationEnabled,
                snoozeLimit = state.snoozeLimit
            )
            
            val result = if (alarmId != null) {
                updateAlarmUseCase(alarm)
                Result.success(alarmId)
            } else {
                createAlarmUseCase(alarm)
            }
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            _saveResult.value = result.isSuccess
            
            // TODO: Schedule the alarm using AlarmScheduler
        }
    }
}
