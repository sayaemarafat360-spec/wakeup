package com.wakeupwarrior.presentation.screens.alarm

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wakeupwarrior.data.model.ChallengeType
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlarmScreen(
    alarmId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: CreateAlarmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val saveResult by viewModel.saveResult.collectAsStateWithLifecycle()
    
    // Handle save result
    LaunchedEffect(saveResult) {
        saveResult?.let { success ->
            if (success) {
                onNavigateBack()
            }
        }
    }
    
    val scrollState = rememberScrollState()
    
    // Time picker state
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Challenge picker state
    var showChallengePicker by remember { mutableStateOf(false) }
    
    GradientBackground {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar
            CreateAlarmTopBar(
                isEditing = alarmId != null,
                onSave = { viewModel.saveAlarm() },
                canSave = uiState.canSave,
                onNavigateBack = onNavigateBack
            )
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Time picker
                TimePickerSection(
                    hour = uiState.hour,
                    minute = uiState.minute,
                    onClick = { showTimePicker = true }
                )
                
                // Label
                GlassTextField(
                    value = uiState.label,
                    onValueChange = { viewModel.updateLabel(it) },
                    placeholder = "Wake up call",
                    label = "Alarm Name",
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Repeat days
                DaySelector(
                    selectedDays = uiState.repeatDays,
                    onDayToggle = { viewModel.toggleDay(it) }
                )
                
                // Challenge type
                ChallengeTypeSection(
                    challengeType = uiState.challengeType,
                    isPremium = isPremium,
                    onClick = { showChallengePicker = true }
                )
                
                // Difficulty
                DifficultySection(
                    difficulty = uiState.difficulty,
                    onDifficultyChange = { viewModel.updateDifficulty(it) }
                )
                
                // Settings
                SettingsSection(
                    vibrationEnabled = uiState.vibrationEnabled,
                    escalationEnabled = uiState.escalationEnabled,
                    snoozeLimit = uiState.snoozeLimit,
                    onVibrationChange = { viewModel.updateVibration(it) },
                    onEscalationChange = { viewModel.updateEscalation(it) },
                    onSnoozeLimitChange = { viewModel.updateSnoozeLimit(it) }
                )
                
                // Spacer for FAB
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
    
    // Time picker dialog
    if (showTimePicker) {
        TimePickerDialog(
            initialHour = uiState.hour,
            initialMinute = uiState.minute,
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                viewModel.updateTime(hour, minute)
                showTimePicker = false
            }
        )
    }
    
    // Challenge picker dialog
    if (showChallengePicker) {
        ChallengePickerDialog(
            selectedType = uiState.challengeType,
            isPremium = isPremium,
            onDismiss = { showChallengePicker = false },
            onSelect = { type ->
                viewModel.updateChallengeType(type)
                showChallengePicker = false
            }
        )
    }
}

@Composable
private fun CreateAlarmTopBar(
    isEditing: Boolean,
    onSave: () -> Unit,
    canSave: Boolean,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 20.dp, top = 48.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
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
            text = if (isEditing) "Edit Alarm" else "New Alarm",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = TextPrimary
        )
        
        TextButton(
            onClick = onSave,
            enabled = canSave
        ) {
            Text(
                text = "Save",
                style = MaterialTheme.typography.labelLarge,
                color = if (canSave) Primary else TextMuted
            )
        }
    }
}

@Composable
private fun TimePickerSection(
    hour: Int,
    minute: Int,
    onClick: () -> Unit
) {
    GlassCardGlow(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        glowColor = Primary.copy(alpha = 0.15f),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Hour
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(GlassBackgroundLight)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", hour),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                }
                
                Text(
                    text = ":",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary
                )
                
                // Minute
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(GlassBackgroundLight)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", minute),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                }
            }
            
            // Hint
            Text(
                text = "Tap to change time",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun DaySelector(
    selectedDays: List<Int>,
    onDayToggle: (Int) -> Unit
) {
    val days = listOf(
        Calendar.SUNDAY to "S",
        Calendar.MONDAY to "M",
        Calendar.TUESDAY to "T",
        Calendar.WEDNESDAY to "W",
        Calendar.THURSDAY to "T",
        Calendar.FRIDAY to "F",
        Calendar.SATURDAY to "S"
    )
    
    Column {
        Text(
            text = "Repeat",
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { (day, label) ->
                val isSelected = selectedDays.contains(day)
                
                GlassCard(
                    modifier = Modifier.size(44.dp),
                    backgroundColor = if (isSelected) Primary.copy(alpha = 0.3f) else GlassBackground,
                    borderColor = if (isSelected) Primary else GlassBorder,
                    cornerRadius = 12.dp,
                    onClick = { onDayToggle(day) }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) Primary else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeTypeSection(
    challengeType: ChallengeType,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = "Challenge Type",
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = challengeType.icon.let { 
                            when (it) {
                                "calculator" -> "🧮"
                                "shake" -> "📳"
                                "brain" -> "🧠"
                                "qr_code" -> "📷"
                                "keyboard" -> "⌨️"
                                "mic" -> "🎤"
                                "directions_walk" -> "🚶"
                                else -> "❓"
                            }
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                    
                    Column {
                        Text(
                            text = challengeType.displayName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = TextPrimary
                        )
                        
                        if (challengeType.isPremium && !isPremium) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Warning,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Premium",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Warning
                                )
                            }
                        }
                    }
                }
                
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextMuted
                )
            }
        }
    }
}

@Composable
private fun DifficultySection(
    difficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit
) {
    Column {
        Text(
            text = "Difficulty",
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Difficulty.values().forEach { diff ->
                val isSelected = difficulty == diff
                val color = when (diff) {
                    Difficulty.EASY -> Success
                    Difficulty.MEDIUM -> Warning
                    Difficulty.HARD -> Error
                    Difficulty.NIGHTMARE -> Tertiary
                }
                
                GlassCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = if (isSelected) color.copy(alpha = 0.2f) else GlassBackground,
                    borderColor = if (isSelected) color else GlassBorder,
                    onClick = { onDifficultyChange(diff) }
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = diff.displayName,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) color else TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    vibrationEnabled: Boolean,
    escalationEnabled: Boolean,
    snoozeLimit: Int,
    onVibrationChange: (Boolean) -> Unit,
    onEscalationChange: (Boolean) -> Unit,
    onSnoozeLimitChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
        
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Vibration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                        Text(
                            text = "Vibration",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                    
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = onVibrationChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Primary,
                            checkedTrackColor = Primary.copy(alpha = 0.5f)
                        )
                    )
                }
                
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = GlassBorder
                )
                
                // Smart Escalation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                        Column {
                            Text(
                                text = "Smart Escalation",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary
                            )
                            Text(
                                text = "Alarm gets louder over time",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                    
                    Switch(
                        checked = escalationEnabled,
                        onCheckedChange = onEscalationChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Primary,
                            checkedTrackColor = Primary.copy(alpha = 0.5f)
                        )
                    )
                }
                
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = GlassBorder
                )
                
                // Snooze limit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Snooze,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                        Text(
                            text = "Snooze Limit",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { if (snoozeLimit > 0) onSnoozeLimitChange(snoozeLimit - 1) },
                            enabled = snoozeLimit > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = null,
                                tint = if (snoozeLimit > 0) TextPrimary else TextMuted
                            )
                        }
                        
                        Text(
                            text = "$snoozeLimit",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                        
                        IconButton(
                            onClick = { if (snoozeLimit < 10) onSnoozeLimitChange(snoozeLimit + 1) },
                            enabled = snoozeLimit < 10
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = if (snoozeLimit < 10) TextPrimary else TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
