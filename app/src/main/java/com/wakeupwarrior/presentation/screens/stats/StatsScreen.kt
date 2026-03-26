package com.wakeupwarrior.presentation.screens.stats

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wakeupwarrior.data.model.UserStats
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*

@Composable
fun StatsScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    
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
                    text = "Your Stats",
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Streak stats
                stats?.let { userStats ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Current Streak",
                            value = "${userStats.currentStreak}",
                            subtitle = "days",
                            emoji = "🔥",
                            modifier = Modifier.weight(1f)
                        )
                        
                        StatCard(
                            title = "Longest Streak",
                            value = "${userStats.longestStreak}",
                            subtitle = "days",
                            emoji = "🏆",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Total Wake-ups",
                            value = "${userStats.totalWakeUps}",
                            subtitle = "all time",
                            emoji = "⏰",
                            modifier = Modifier.weight(1f)
                        )
                        
                        StatCard(
                            title = "Challenges",
                            value = "${userStats.totalChallengesCompleted}",
                            subtitle = "completed",
                            emoji = "🧩",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Coins
                    GlassCardGlow(
                        modifier = Modifier.fillMaxWidth(),
                        glowColor = Warning.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Coins",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextMuted
                                )
                                
                                Text(
                                    text = "${userStats.coins}",
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Warning
                                )
                            }
                            
                            Text(
                                text = "🪙",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = MaterialTheme.typography.displayLarge.fontSize * 2
                                )
                            )
                        }
                    }
                    
                    // Premium status
                    if (userStats.hasValidPremium) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "⭐", style = MaterialTheme.typography.headlineMedium)
                                    Column {
                                        Text(
                                            text = "Premium Active",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = Success
                                        )
                                        Text(
                                            text = "All features unlocked",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}
