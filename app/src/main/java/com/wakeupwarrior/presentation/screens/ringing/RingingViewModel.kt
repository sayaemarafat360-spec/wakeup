package com.wakeupwarrior.presentation.screens.ringing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.data.model.Alarm
import com.wakeupwarrior.data.model.ChallengeType
import com.wakeupwarrior.domain.repository.AlarmRepository
import com.wakeupwarrior.domain.usecase.alarm.IncrementSnoozeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RingingViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val incrementSnoozeUseCase: IncrementSnoozeUseCase
) : ViewModel() {
    
    private val _alarm = MutableStateFlow<Alarm?>(null)
    val alarm = _alarm.asStateFlow()
    
    private val _snoozesRemaining = MutableStateFlow(0)
    val snoozesRemaining = _snoozesRemaining.asStateFlow()
    
    private val _elapsedTime = MutableStateFlow(0)
    val elapsedTime = _elapsedTime.asStateFlow()
    
    private var startTime = 0L
    
    fun loadAlarm(alarmId: Long) {
        viewModelScope.launch {
            val alarmData = alarmRepository.getAlarmById(alarmId)
            _alarm.value = alarmData
            _snoozesRemaining.value = (alarmData?.snoozeLimit ?: 0) - (alarmData?.snoozesUsed ?: 0)
            
            // Start tracking elapsed time
            startTime = System.currentTimeMillis()
            launch {
                while (true) {
                    delay(1000)
                    _elapsedTime.value = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                }
            }
            
            // TODO: Start playing alarm sound and vibration
        }
    }
    
    fun snooze() {
        viewModelScope.launch {
            _alarm.value?.let { alarmData ->
                val success = incrementSnoozeUseCase(alarmData.id)
                if (success) {
                    _snoozesRemaining.value = _snoozesRemaining.value - 1
                    // TODO: Schedule snooze (5 minutes)
                    // TODO: Stop current alarm
                }
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        // TODO: Stop alarm sound and vibration
    }
}
