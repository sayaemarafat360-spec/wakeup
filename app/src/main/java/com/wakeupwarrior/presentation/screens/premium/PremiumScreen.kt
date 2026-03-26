package com.wakeupwarrior.presentation.screens.premium

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*

@Composable
fun PremiumScreen(
    onNavigateBack: () -> Unit,
    viewModel: PremiumViewModel = hiltViewModel()
) {
    val selectedPlan by viewModel.selectedPlan.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    GradientBackground {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 20.dp, top = 48.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                
                Text(
                    text = "Go Premium",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary
                )
            }
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hero section
                Text(
                    text = "⭐",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = MaterialTheme.typography.displayLarge.fontSize * 2
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Unlock Your Full Potential",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Get unlimited alarms, all challenge types, custom sounds, and more!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Features list
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PremiumFeature(text = "Unlimited alarms", icon = "⏰")
                        PremiumFeature(text = "All challenge types", icon = "🧩")
                        PremiumFeature(text = "Custom alarm sounds", icon = "🎵")
                        PremiumFeature(text = "Advanced statistics", icon = "📊")
                        PremiumFeature(text = "Premium themes", icon = "🎨")
                        PremiumFeature(text = "No advertisements", icon = "🚫")
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Plan selection
                Text(
                    text = "Choose Your Plan",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PlanCard(
                        title = "Monthly",
                        price = "$2.99",
                        period = "/month",
                        isSelected = selectedPlan == "monthly",
                        onClick = { viewModel.selectPlan("monthly") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    PlanCard(
                        title = "Yearly",
                        price = "$19.99",
                        period = "/year",
                        subtitle = "Save 44%",
                        isSelected = selectedPlan == "yearly",
                        onClick = { viewModel.selectPlan("yearly") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Lifetime option
                PlanCard(
                    title = "Lifetime",
                    price = "$49.99",
                    period = "once",
                    subtitle = "Best Value",
                    isSelected = selectedPlan == "lifetime",
                    onClick = { viewModel.selectPlan("lifetime") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Subscribe button
                GlassButton(
                    text = if (isLoading) "Processing..." else "Subscribe Now",
                    onClick = { viewModel.purchase() },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Restore purchase
                TextButton(onClick = { viewModel.restorePurchase() }) {
                    Text(
                        text = "Restore Purchase",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Cancel anytime. Payment will be charged to your Google Play account.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PremiumFeature(
    text: String,
    icon: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleMedium
        )
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Success
        )
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    period: String,
    subtitle: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        backgroundColor = if (isSelected) Primary.copy(alpha = 0.2f) else GlassBackground,
        borderColor = if (isSelected) Primary else GlassBorder,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (subtitle != null) {
                Surface(
                    color = Primary,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (isSelected) Primary else TextPrimary
            )
            
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = price,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isSelected) Primary else TextPrimary
                )
                Text(
                    text = period,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

// Need to import Color
import androidx.compose.ui.graphics.Color
