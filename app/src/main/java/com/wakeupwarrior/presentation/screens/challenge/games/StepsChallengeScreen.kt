package com.wakeupwarrior.presentation.screens.challenge.games

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*

@Composable
fun StepsChallengeScreen(
    difficulty: Difficulty,
    onComplete: () -> Unit
) {
    val targetSteps = when (difficulty) {
        Difficulty.EASY -> 10
        Difficulty.MEDIUM -> 25
        Difficulty.HARD -> 50
        Difficulty.NIGHTMARE -> 100
    }
    
    var stepCount by remember { mutableStateOf(0) }
    var isComplete by remember { mutableStateOf(false) }
    
    // Simulate step counting (in real app, use Step Counter Sensor)
    // For demo purposes, we'll use a button to add steps
    
    // Animation
    val progress by animateFloatAsState(
        targetValue = stepCount.toFloat() / targetSteps.toFloat(),
        animationSpec = tween(200),
        label = "progress"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isComplete) 1.2f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    
    // Check completion
    LaunchedEffect(stepCount) {
        if (stepCount >= targetSteps) {
            isComplete = true
            kotlinx.coroutines.delay(500)
            onComplete()
        }
    }
    
    // Note: In a real implementation, you would use:
    // - Sensor.TYPE_STEP_COUNTER for Android 4.4+
    // - Or Sensor.TYPE_ACCELEROMETER for step detection
    // This requires ACTIVITY_RECOGNITION permission on Android 10+
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Header
        Text(
            text = "Steps Challenge",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Walk $targetSteps steps to wake up",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Progress circle
        Box(
            modifier = Modifier.size(250.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgress(
                progress = progress,
                strokeWidth = 16.dp,
                progressColor = Primary,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🚶",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = MaterialTheme.typography.displayLarge.fontSize * 2
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "$stepCount",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "of $targetSteps steps",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Instructions / Demo button
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "How it works",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Walk around your room with your phone in your pocket or hand. The step counter will track your movement.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Demo button (for testing without walking)
                Text(
                    text = "Demo Mode",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                GlassButtonSecondary(
                    text = "Add 5 Steps (Demo)",
                    onClick = { 
                        stepCount = (stepCount + 5).coerceAtMost(targetSteps) 
                    },
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Status
        AnimatedContent(
            targetState = targetSteps - stepCount,
            label = "remaining"
        ) { remaining ->
            Text(
                text = when {
                    isComplete -> "Complete! 🎉"
                    remaining > 0 -> "$remaining steps to go!"
                    else -> "Complete! 🎉"
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (remaining <= 0) Success else Primary
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Difficulty indicator
        Text(
            text = "Difficulty: ${difficulty.displayName}",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}
