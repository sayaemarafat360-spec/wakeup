package com.wakeupwarrior.presentation.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wakeupwarrior.data.model.Alarm
import com.wakeupwarrior.data.model.UserStats
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*
import com.wakeupwarrior.core.util.getTimeOfDay
import com.wakeupwarrior.core.util.calculateNextAlarmTime
import com.wakeupwarrior.core.util.toTimeUntilString

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onNavigateToCreateAlarm: (Long?) -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPremium: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val alarms by viewModel.alarms.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    val timeOfDay = remember { getTimeOfDay() }
    
    // Animation states
    var contentVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        contentVisible = true
    }
    
    GradientBackgroundWithOrbs {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                // Top bar
                HomeTopBar(
                    stats = stats,
                    onNavigateToSettings = onNavigateToSettings,
                    onNavigateToPremium = onNavigateToPremium,
                    modifier = Modifier.padding(top = 48.dp)
                )
                
                // Greeting
                GreetingSection(
                    timeOfDay = timeOfDay,
                    streak = stats?.currentStreak ?: 0,
                    modifier = Modifier.padding(top = 24.dp)
                )
                
                // Stats row
                StatsRow(
                    stats = stats,
                    onNavigateToStats = onNavigateToStats,
                    onNavigateToAchievements = onNavigateToAchievements,
                    modifier = Modifier.padding(top = 24.dp)
                )
                
                // Next alarm card
                alarms.firstOrNull { it.isEnabled }?.let { alarm ->
                    NextAlarmCard(
                        alarm = alarm,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
                
                // Alarms header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Alarms",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "${alarms.size} alarms",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
                
                // Alarms list
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Primary
                        )
                    }
                } else if (alarms.isEmpty()) {
                    EmptyAlarmsState(
                        onAddAlarm = { onNavigateToCreateAlarm(null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(alarms, key = { it.id }) { alarm ->
                            AlarmCard(
                                alarm = alarm,
                                onToggle = { enabled ->
                                    viewModel.toggleAlarm(alarm.id, enabled)
                                },
                                onEdit = {
                                    onNavigateToCreateAlarm(alarm.id)
                                },
                                onDelete = {
                                    viewModel.deleteAlarm(alarm)
                                }
                            )
                        }
                    }
                }
            }
            
            // FAB
            FloatingActionButton(
                onClick = { onNavigateToCreateAlarm(null) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .size(64.dp),
                containerColor = Primary,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Alarm",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    stats: UserStats?,
    onNavigateToSettings: () -> Unit,
    onNavigateToPremium: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⏰",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "WakeUp Warrior",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
        }
        
        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Premium badge
            if (stats?.hasValidPremium != true) {
                GlassIconButton(
                    onClick = onNavigateToPremium,
                    backgroundColor = Primary.copy(alpha = 0.2f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Premium",
                        tint = Warning
                    )
                }
            }
            
            // Settings
            GlassIconButton(onClick = onNavigateToSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun GreetingSection(
    timeOfDay: String,
    streak: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Good ${timeOfDay.replaceFirstChar { it.uppercase() }}, Warrior!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        
        if (streak > 0) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔥",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$streak day streak! Keep it up!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Warning
                )
            }
        }
    }
}

@Composable
private fun StatsRow(
    stats: UserStats?,
    onNavigateToStats: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StreakBadge(
            streak = stats?.currentStreak ?: 0,
            modifier = Modifier.weight(1f)
        )
        
        CoinDisplay(
            coins = stats?.coins ?: 0,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NextAlarmCard(
    alarm: Alarm,
    modifier: Modifier = Modifier
) {
    val nextTime = calculateNextAlarmTime(alarm.hour, alarm.minute, alarm.repeatDays)
    val timeUntil = nextTime.toTimeUntilString()
    
    GlassCardGlow(
        modifier = modifier.fillMaxWidth(),
        glowColor = Primary.copy(alpha = 0.2f)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Next Alarm",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
                
                Text(
                    text = "in $timeUntil",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = alarm.displayTime,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = alarm.label.ifEmpty { alarm.repeatDaysText },
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun AlarmCard(
    alarm: Alarm,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEdit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = alarm.displayTime,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (alarm.isEnabled) TextPrimary else TextMuted
                )
                
                Text(
                    text = alarm.label.ifEmpty { alarm.repeatDaysText },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = alarm.challengeType.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = Primary
                    )
                    
                    Text(
                        text = "•",
                        color = TextMuted
                    )
                    
                    Text(
                        text = alarm.difficulty.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = when (alarm.difficulty) {
                            com.wakeupwarrior.data.model.Difficulty.EASY -> Success
                            com.wakeupwarrior.data.model.Difficulty.MEDIUM -> Warning
                            com.wakeupwarrior.data.model.Difficulty.HARD -> Error
                            com.wakeupwarrior.data.model.Difficulty.NIGHTMARE -> Tertiary
                        }
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Primary,
                        checkedTrackColor = Primary.copy(alpha = 0.5f)
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                IconButton(
                    onClick = { showDeleteConfirm = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Error.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Alarm?") },
            text = { Text("Are you sure you want to delete this alarm?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    }
                ) {
                    Text("Delete", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            },
            containerColor = Surface,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary
        )
    }
}

@Composable
private fun EmptyAlarmsState(
    onAddAlarm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⏰",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = MaterialTheme.typography.displayLarge.fontSize * 2
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No Alarms Yet",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Tap the + button to create your first alarm",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        GlassButton(
            text = "Create Alarm",
            onClick = onAddAlarm
        )
    }
}

// Need to import Color
import androidx.compose.ui.graphics.Color
