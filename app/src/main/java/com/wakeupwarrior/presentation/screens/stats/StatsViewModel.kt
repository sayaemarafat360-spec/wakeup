package com.wakeupwarrior.presentation.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.data.model.UserStats
import com.wakeupwarrior.domain.usecase.stats.GetStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    getStatsUseCase: GetStatsUseCase
) : ViewModel() {
    
    val stats = getStatsUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
}
