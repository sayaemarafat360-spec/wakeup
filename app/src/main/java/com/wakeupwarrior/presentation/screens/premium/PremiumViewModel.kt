package com.wakeupwarrior.presentation.screens.premium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupwarrior.domain.usecase.stats.UpdatePremiumStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val updatePremiumStatusUseCase: UpdatePremiumStatusUseCase
) : ViewModel() {
    
    private val _selectedPlan = MutableStateFlow("yearly")
    val selectedPlan = _selectedPlan.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    fun selectPlan(plan: String) {
        _selectedPlan.value = plan
    }
    
    fun purchase() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // TODO: Implement actual Google Play Billing
            // For now, simulate a purchase
            kotlinx.coroutines.delay(2000)
            
            // Update premium status based on selected plan
            when (_selectedPlan.value) {
                "monthly" -> {
                    val until = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)
                    updatePremiumStatusUseCase(false, until)
                }
                "yearly" -> {
                    val until = System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000)
                    updatePremiumStatusUseCase(false, until)
                }
                "lifetime" -> {
                    updatePremiumStatusUseCase(true, null)
                }
            }
            
            _isLoading.value = false
        }
    }
    
    fun restorePurchase() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // TODO: Implement actual restore with Google Play Billing
            kotlinx.coroutines.delay(1000)
            
            _isLoading.value = false
        }
    }
}
