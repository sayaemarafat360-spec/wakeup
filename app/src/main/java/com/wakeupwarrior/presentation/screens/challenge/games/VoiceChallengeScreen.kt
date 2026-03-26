package com.wakeupwarrior.presentation.screens.challenge.games

import android.Manifest
import android.content.pm.PackageManager
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*

val wakeUpPhrases = listOf(
    "I am awake and ready",
    "Good morning world",
    "Today will be great",
    "I am ready to conquer",
    "Rise and shine"
)

@Composable
fun VoiceChallengeScreen(
    difficulty: Difficulty,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    var targetPhrase by remember { mutableStateOf(wakeUpPhrases.random()) }
    var spokenText by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }
    var isComplete by remember { mutableStateOf(false) }
    var attempts by remember { mutableStateOf(0) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
    }
    
    LaunchedEffect(Unit) {
        if (!hasAudioPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
    
    // Check completion
    LaunchedEffect(spokenText) {
        val similarity = calculateSimilarity(spokenText.lowercase(), targetPhrase.lowercase())
        if (similarity >= 0.8f) {
            isComplete = true
            kotlinx.coroutines.delay(500)
            onComplete()
        }
    }
    
    // Animation for mic button
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Header
        Text(
            text = "Voice Challenge",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Say the phrase out loud",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Target phrase
        GlassCardGlow(
            modifier = Modifier.fillMaxWidth(),
            glowColor = Primary.copy(alpha = 0.1f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Say:",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "\"$targetPhrase\"",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Mic button
        if (hasAudioPermission) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                GlassCardGlow(
                    modifier = Modifier.fillMaxSize(),
                    glowColor = if (isListening) Primary.copy(alpha = 0.5f) else Primary.copy(alpha = 0.2f),
                    onClick = {
                        if (!isListening) {
                            isListening = true
                            // Simulate speech recognition (in real app, use SpeechRecognizer)
                            kotlinx.coroutines.GlobalScope.launch {
                                kotlinx.coroutines.delay(3000)
                                spokenText = targetPhrase // Placeholder - would be actual speech result
                                isListening = false
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Microphone",
                            modifier = Modifier.size(48.dp),
                            tint = if (isListening) Primary else TextSecondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (isListening) "Listening..." else "Tap to speak",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isListening) Primary else TextMuted
            )
        } else {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎤",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.5
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        GlassButton(
                            text = "Grant Permission",
                            onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }
                        )
                    }
                }
            }
        }
        
        // Spoken text display
        AnimatedVisibility(
            visible = spokenText.isNotEmpty() && !isComplete,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut()
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "You said:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "\"$spokenText\"",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            }
        }
        
        // Success message
        AnimatedVisibility(
            visible = isComplete,
            enter = fadeIn() + scaleIn()
        ) {
            Text(
                text = "✓ Perfect! You're awake!",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Success,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Difficulty indicator
        Text(
            text = "Difficulty: ${difficulty.displayName}",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

private fun calculateSimilarity(s1: String, s2: String): Float {
    if (s1 == s2) return 1f
    if (s1.isEmpty() || s2.isEmpty()) return 0f
    
    val longer = if (s1.length > s2.length) s1 else s2
    val shorter = if (s1.length > s2.length) s2 else s1
    
    val longerLength = longer.length
    if (longerLength == 0) return 1f
    
    return (longerLength - levenshteinDistance(longer, shorter)) / longerLength.toFloat()
}

private fun levenshteinDistance(s1: String, s2: String): Int {
    val costs = IntArray(s2.length + 1)
    for (i in 0..s2.length) costs[i] = i
    
    for (i in 1..s1.length) {
        costs[0] = i
        var nw = i - 1
        for (j in 1..s2.length) {
            val cj = costs[j].coerceAtMost(costs[j - 1] + 1)
                .coerceAtMost(if (s1[i - 1] == s2[j - 1]) nw else nw + 1)
            nw = costs[j]
            costs[j] = cj
        }
    }
    return costs[s2.length]
}
