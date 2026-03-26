package com.wakeupwarrior.presentation.screens.challenge.games

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MemoryChallengeScreen(
    difficulty: Difficulty,
    onComplete: () -> Unit
) {
    val gridSize = when (difficulty) {
        Difficulty.EASY -> 3
        Difficulty.MEDIUM -> 4
        Difficulty.HARD -> 5
        Difficulty.NIGHTMARE -> 6
    }
    
    val patternLength = when (difficulty) {
        Difficulty.EASY -> 3
        Difficulty.MEDIUM -> 4
        Difficulty.HARD -> 6
        Difficulty.NIGHTMARE -> 8
    }
    
    val colors = listOf(Primary, Secondary, Tertiary, Success, Warning, Error)
    
    var pattern by remember { mutableStateOf<List<Int>>(emptyList()) }
    var userPattern by remember { mutableStateOf<List<Int>>(emptyList()) }
    var isShowingPattern by remember { mutableStateOf(true) }
    var currentShowIndex by remember { mutableStateOf(0) }
    var phase by remember { mutableStateOf("memorize") } // memorize, input, success, fail
    
    // Generate pattern
    LaunchedEffect(Unit) {
        pattern = List(patternLength) { Random.nextInt(gridSize * gridSize) }
        delay(500)
        
        // Show pattern
        for (i in pattern.indices) {
            currentShowIndex = i
            delay(600)
        }
        
        delay(300)
        isShowingPattern = false
        phase = "input"
    }
    
    // Check user pattern
    LaunchedEffect(userPattern.size) {
        if (userPattern.size == pattern.size && phase == "input") {
            if (userPattern == pattern) {
                phase = "success"
                delay(500)
                onComplete()
            } else {
                phase = "fail"
                delay(1000)
                // Reset
                userPattern = emptyList()
                pattern = List(patternLength) { Random.nextInt(gridSize * gridSize) }
                isShowingPattern = true
                phase = "memorize"
                
                for (i in pattern.indices) {
                    currentShowIndex = i
                    delay(600)
                }
                delay(300)
                isShowingPattern = false
                phase = "input"
            }
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
            text = "Memory Challenge",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = when (phase) {
                "memorize" -> "Memorize the pattern!"
                "input" -> "Repeat the pattern"
                "success" -> "Correct! 🎉"
                "fail" -> "Wrong! Try again"
                else -> ""
            },
            style = MaterialTheme.typography.bodyLarge,
            color = when (phase) {
                "memorize" -> Warning
                "input" -> Primary
                "success" -> Success
                "fail" -> Error
                else -> TextSecondary
            }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(gridSize * gridSize) { index ->
                val isPatternCell = pattern.contains(index)
                val isCurrentHighlight = isShowingPattern && 
                    currentShowIndex < pattern.size && 
                    pattern[currentShowIndex] == index
                val isUserSelected = userPattern.contains(index)
                val isLastSelected = userPattern.isNotEmpty() && userPattern.last() == index
                
                val cellColor = when {
                    isCurrentHighlight -> Primary
                    isUserSelected -> Secondary.copy(alpha = 0.7f)
                    else -> GlassBackground
                }
                
                val scale by animateFloatAsState(
                    targetValue = when {
                        isCurrentHighlight -> 1.1f
                        isLastSelected -> 1.05f
                        else -> 1f
                    },
                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                    label = "cellScale"
                )
                
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .scale(scale)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cellColor)
                        .then(
                            if (phase == "input" && !isUserSelected) {
                                Modifier.clickable {
                                    userPattern = userPattern + index
                                }
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCurrentHighlight) {
                        Text(
                            text = "${currentShowIndex + 1}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Progress
        if (phase == "input") {
            Text(
                text = "${userPattern.size} / ${pattern.size} cells selected",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
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
