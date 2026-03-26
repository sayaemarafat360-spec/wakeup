package com.wakeupwarrior.presentation.screens.ringing

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wakeupwarrior.data.model.Alarm
import com.wakeupwarrior.data.model.ChallengeType
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*

@Composable
fun RingingScreen(
    alarmId: Long,
    onDismiss: () -> Unit,
    onNavigateToChallenge: (Long, ChallengeType) -> Unit,
    viewModel: RingingViewModel = hiltViewModel()
) {
    val alarm by viewModel.alarm.collectAsStateWithLifecycle()
    val snoozesRemaining by viewModel.snoozesRemaining.collectAsStateWithLifecycle()
    val elapsedTime by viewModel.elapsedTime.collectAsStateWithLifecycle()
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "ringing")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    val iconRotation by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    
    LaunchedEffect(alarmId) {
        viewModel.loadAlarm(alarmId)
    }
    
    AlarmPulseBackground(
        pulseColor = Error.copy(alpha = 0.2f + (elapsedTime / 120f * 0.1f).coerceIn(0f, 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // Animated alarm icon
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationZ = iconRotation
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⏰",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = MaterialTheme.typography.displayLarge.fontSize * 3
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Wake up text
            Text(
                text = "WAKE UP!",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Error
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Time
            Text(
                text = alarm?.displayTime ?: "",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
            
            // Label
            alarm?.label?.let { label ->
                if (label.isNotEmpty()) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Challenge info
            alarm?.let { alarmData ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Complete challenge to dismiss",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = alarmData.challengeType.icon.let {
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
                            
                            Text(
                                text = alarmData.challengeType.displayName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextPrimary
                            )
                            
                            Text(
                                text = "•",
                                color = TextMuted
                            )
                            
                            Text(
                                text = alarmData.difficulty.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                color = when (alarmData.difficulty) {
                                    com.wakeupwarrior.data.model.Difficulty.EASY -> Success
                                    com.wakeupwarrior.data.model.Difficulty.MEDIUM -> Warning
                                    com.wakeupwarrior.data.model.Difficulty.HARD -> Error
                                    com.wakeupwarrior.data.model.Difficulty.NIGHTMARE -> Tertiary
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Start Challenge Button
            GlassButtonGlow(
                text = "Start Challenge",
                onClick = {
                    alarm?.let { alarmData ->
                        onNavigateToChallenge(alarmData.id, alarmData.challengeType)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Snooze button
            if (snoozesRemaining > 0) {
                GlassButtonSecondary(
                    text = "Snooze ($snoozesRemaining left)",
                    onClick = { viewModel.snooze() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
