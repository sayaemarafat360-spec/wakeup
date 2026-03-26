package com.wakeupwarrior.presentation.screens.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.core.util.Constants
import com.wakeupwarrior.domain.usecase.stats.CheckPremiumStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val checkPremiumStatusUseCase: CheckPremiumStatusUseCase
) : ViewModel() {
    
    private val _isPremium = MutableStateFlow(false)
    val isPremium = _isPremium.asStateFlow()
    
    private val _vibrationEnabled = MutableStateFlow(true)
    val vibrationEnabled = _vibrationEnabled.asStateFlow()
    
    private val _escalationEnabled = MutableStateFlow(true)
    val escalationEnabled = _escalationEnabled.asStateFlow()
    
    init {
        viewModelScope.launch {
            _isPremium.value = checkPremiumStatusUseCase()
            
            dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey(Constants.Preferences.KEY_VIBRATION_ENABLED)] ?: true
            }.first().let { _vibrationEnabled.value = it }
            
            dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey(Constants.Preferences.KEY_ESCALATION_ENABLED)] ?: true
            }.first().let { _escalationEnabled.value = it }
        }
    }
    
    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[booleanPreferencesKey(Constants.Preferences.KEY_VIBRATION_ENABLED)] = enabled
            }
            _vibrationEnabled.value = enabled
        }
    }
    
    fun setEscalationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[booleanPreferencesKey(Constants.Preferences.KEY_ESCALATION_ENABLED)] = enabled
            }
            _escalationEnabled.value = enabled
        }
    }
}
