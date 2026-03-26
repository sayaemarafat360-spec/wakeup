package com.wakeupwarrior.presentation.screens.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.data.model.Achievement
import com.wakeupwarrior.domain.usecase.achievement.GetAchievementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    getAchievementsUseCase: GetAchievementsUseCase
) : ViewModel() {
    
    val achievements = getAchievementsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}
