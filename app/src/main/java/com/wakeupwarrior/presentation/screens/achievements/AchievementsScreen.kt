package com.wakeupwarrior.presentation.screens.achievements

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.wakeupwarrior.data.model.Achievement
import com.wakeupwarrior.data.model.AchievementDefinitions
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*

@Composable
fun AchievementsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AchievementsViewModel = hiltViewModel()
) {
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    
    val achievementDefs = remember { AchievementDefinitions.all }
    
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
                    text = "Achievements",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextPrimary
                )
            }
            
            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(achievementDefs) { definition ->
                    val achievement = achievements.find { it.id == definition.id }
                    AchievementCard(
                        definition = definition,
                        achievement = achievement
                    )
                }
            }
        }
    }
}

@Composable
private fun AchievementCard(
    definition: com.wakeupwarrior.data.model.AchievementDefinition,
    achievement: Achievement?
) {
    val isUnlocked = achievement?.isUnlocked ?: false
    val progress = achievement?.progress ?: 0
    
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (isUnlocked) Success.copy(alpha = 0.1f) else GlassBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(
                        text = definition.icon.let { 
                            when (it) {
                                "star" -> "⭐"
                                "fire" -> "🔥"
                                "crown" -> "👑"
                                "wb_sunny" -> "☀️"
                                "emoji_events" -> "🏆"
                                "block" -> "🚫"
                                "calculate" -> "🧮"
                                "psychology" -> "🧠"
                                "vibration" -> "📳"
                                "nightlight" -> "🌙"
                                "schedule" -> "⏰"
                                "monetization_on" -> "💰"
                                else -> "🏅"
                            }
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                } else {
                    Text(
                        text = "🔒",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = definition.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isUnlocked) Success else TextPrimary
                )
                
                Text(
                    text = definition.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                
                if (!isUnlocked && progress > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    AnimatedProgressBar(
                        progress = progress.toFloat() / definition.targetProgress,
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(Primary, Secondary)
                    )
                    
                    Text(
                        text = "$progress / ${definition.targetProgress}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // Reward
            if (definition.coinsReward > 0) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "+${definition.coinsReward}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (isUnlocked) Success else Warning
                    )
                    
                    Text(
                        text = "coins",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
