package com.wakeupwarrior.presentation.screens.challenge.games

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*
import kotlin.random.Random

val motivationalQuotes = listOf(
    "The early bird catches the worm.",
    "Rise and shine, it's a brand new day!",
    "Today is your opportunity to build the tomorrow you want.",
    "Every morning brings new potential.",
    "Wake up with determination, go to bed with satisfaction.",
    "The future belongs to those who believe in the beauty of their dreams.",
    "Success is not final, failure is not fatal.",
    "Believe you can and you're halfway there.",
    "Your only limit is your mind.",
    "Dream big and dare to fail.",
    "Do something today that your future self will thank you for.",
    "The harder you work, the luckier you get.",
    "Don't watch the clock; do what it does. Keep going.",
    "Everything you've ever wanted is on the other side of fear.",
    "Opportunities don't happen, you create them."
)

@Composable
fun TypingChallengeScreen(
    difficulty: Difficulty,
    onComplete: () -> Unit
) {
    val minChars = when (difficulty) {
        Difficulty.EASY -> 10
        Difficulty.MEDIUM -> 20
        Difficulty.HARD -> 35
        Difficulty.NIGHTMARE -> 50
    }
    
    var targetText by remember { mutableStateOf(motivationalQuotes.random()) }
    var typedText by remember { mutableStateOf("") }
    var isComplete by remember { mutableStateOf(false) }
    
    // Calculate progress
    val correctChars = typedText.takeWhileIndexed { index, char ->
        index < targetText.length && char == targetText[index]
    }.length
    
    val accuracy = if (typedText.isNotEmpty()) {
        (correctChars.toFloat() / typedText.length * 100).toInt()
    } else 100
    
    val isCorrect = typedText == targetText.substring(0, minOf(typedText.length, targetText.length))
    
    // Check completion
    LaunchedEffect(typedText) {
        if (typedText == targetText && typedText.length >= minChars) {
            isComplete = true
            kotlinx.coroutines.delay(500)
            onComplete()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        // Header
        Text(
            text = "Typing Challenge",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Type the quote exactly as shown",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Target text
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Type this:",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = targetText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Input
        GlassTextFieldLarge(
            value = typedText,
            onValueChange = { typedText = it },
            placeholder = "Start typing...",
            maxLines = 5
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Progress indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Character count
            GlassCard(
                modifier = Modifier.weight(1f),
                cornerRadius = 16.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${typedText.length}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (typedText.length >= minChars) Success else TextPrimary
                    )
                    
                    Text(
                        text = "/ $minChars chars",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
            
            // Accuracy
            GlassCard(
                modifier = Modifier.weight(1f),
                cornerRadius = 16.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$accuracy%",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (accuracy >= 90) Success else if (accuracy >= 70) Warning else Error
                    )
                    
                    Text(
                        text = "accuracy",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Status
        AnimatedVisibility(
            visible = isComplete,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "✓ Perfect! You're awake!",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Success
            )
        }
        
        AnimatedVisibility(
            visible = !isCorrect && typedText.isNotEmpty() && !isComplete,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "⚠️ Check for typos",
                style = MaterialTheme.typography.bodyMedium,
                color = Warning
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

// Helper extension function
private inline fun String.takeWhileIndexed(predicate: (Int, Char) -> Boolean): String {
    val result = StringBuilder()
    for ((index, char) in this.withIndex()) {
        if (predicate(index, char)) {
            result.append(char)
        } else {
            break
        }
    }
    return result.toString()
}
