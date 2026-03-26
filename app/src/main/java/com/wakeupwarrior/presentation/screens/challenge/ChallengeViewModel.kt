package com.wakeupwarrior.presentation.screens.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.data.model.ChallengeType
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.domain.repository.AlarmRepository
import com.wakeupwarrior.domain.usecase.achievement.CheckAndAwardAchievementsUseCase
import com.wakeupwarrior.domain.usecase.stats.RecordWakeUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val recordWakeUpUseCase: RecordWakeUpUseCase,
    private val checkAndAwardAchievementsUseCase: CheckAndAwardAchievementsUseCase
) : ViewModel() {
    
    private val _difficulty = MutableStateFlow(Difficulty.MEDIUM)
    val difficulty = _difficulty.asStateFlow()
    
    private val _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()
    
    private var currentAlarmId: Long = 0
    private var currentChallengeType: ChallengeType = ChallengeType.MATH
    
    fun loadChallenge(alarmId: Long, challengeType: ChallengeType) {
        viewModelScope.launch {
            currentAlarmId = alarmId
            currentChallengeType = challengeType
            
            val alarm = alarmRepository.getAlarmById(alarmId)
            _difficulty.value = alarm?.difficulty ?: Difficulty.MEDIUM
        }
    }
    
    fun completeChallenge() {
        viewModelScope.launch {
            // Record wake up and award coins
            val coinsEarned = recordWakeUpUseCase()
            
            // Check achievements
            checkAndAwardAchievementsUseCase()
            
            // Reset alarm snoozes
            alarmRepository.resetSnoozes(currentAlarmId)
            
            // Mark as completed
            _isCompleted.value = true
        }
    }
}
