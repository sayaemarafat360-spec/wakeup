package com.wakeupwarrior.presentation.screens.challenge.games

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*
import kotlin.random.Random

data class MathProblem(
    val expression: String,
    val answer: Int
)

@Composable
fun MathChallengeScreen(
    difficulty: Difficulty,
    onComplete: () -> Unit
) {
    var problemsSolved by remember { mutableStateOf(0) }
    val problemsNeeded = when (difficulty) {
        Difficulty.EASY -> 1
        Difficulty.MEDIUM -> 2
        Difficulty.HARD -> 3
        Difficulty.NIGHTMARE -> 5
    }
    
    var currentProblem by remember { mutableStateOf(generateProblem(difficulty)) }
    var userInput by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var attempts by remember { mutableStateOf(0) }
    
    // Animation states
    val scale by animateFloatAsState(
        targetValue = if (isCorrect == true) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    
    LaunchedEffect(isCorrect) {
        if (isCorrect == true) {
            kotlinx.coroutines.delay(500)
            problemsSolved++
            
            if (problemsSolved >= problemsNeeded) {
                onComplete()
            } else {
                currentProblem = generateProblem(difficulty)
                userInput = ""
                isCorrect = null
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
            text = "Math Challenge",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Progress
        Text(
            text = "Solve $problemsNeeded problem${if (problemsNeeded > 1) "s" else ""} • $problemsSolved done",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Problem display
        AnimatedContent(
            targetState = currentProblem,
            transitionSpec = {
                slideInVertically() togetherWith slideOutVertically()
            },
            label = "problem"
        ) { problem ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Solve:",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextMuted
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = problem.expression,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "=",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextMuted
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Answer input
        NumberInputField(
            value = userInput,
            onValueChange = { userInput = it },
            modifier = Modifier.width(150.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Feedback
        AnimatedVisibility(
            visible = isCorrect != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isCorrect == true) "✓ Correct!" else "✗ Try again",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isCorrect == true) Success else Error
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Submit button
        GlassButton(
            text = "Submit",
            onClick = {
                val answer = userInput.toIntOrNull()
                if (answer == currentProblem.answer) {
                    isCorrect = true
                } else {
                    isCorrect = false
                    attempts++
                    if (attempts >= 3) {
                        // Generate new problem after 3 failed attempts
                        currentProblem = generateProblem(difficulty)
                        attempts = 0
                    }
                    kotlinx.coroutines.GlobalScope.launch {
                        kotlinx.coroutines.delay(1000)
                        isCorrect = null
                    }
                }
            },
            enabled = userInput.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.6f)
        )
        
        // Difficulty indicator
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "Difficulty: ${difficulty.displayName}",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

private fun generateProblem(difficulty: Difficulty): MathProblem {
    return when (difficulty) {
        Difficulty.EASY -> generateEasyProblem()
        Difficulty.MEDIUM -> generateMediumProblem()
        Difficulty.HARD -> generateHardProblem()
        Difficulty.NIGHTMARE -> generateNightmareProblem()
    }
}

private fun generateEasyProblem(): MathProblem {
    val a = Random.nextInt(1, 10)
    val b = Random.nextInt(1, 10)
    val operation = Random.nextInt(2)
    
    return when (operation) {
        0 -> MathProblem("$a + $b = ?", a + b)
        1 -> MathProblem("${maxOf(a, b)} - ${minOf(a, b)} = ?", maxOf(a, b) - minOf(a, b))
        else -> MathProblem("$a + $b = ?", a + b)
    }
}

private fun generateMediumProblem(): MathProblem {
    val a = Random.nextInt(10, 50)
    val b = Random.nextInt(10, 50)
    val operation = Random.nextInt(3)
    
    return when (operation) {
        0 -> MathProblem("$a + $b = ?", a + b)
        1 -> MathProblem("${maxOf(a, b)} - ${minOf(a, b)} = ?", maxOf(a, b) - minOf(a, b))
        2 -> {
            val x = Random.nextInt(2, 10)
            val y = Random.nextInt(2, 10)
            MathProblem("$x × $y = ?", x * y)
        }
        else -> MathProblem("$a + $b = ?", a + b)
    }
}

private fun generateHardProblem(): MathProblem {
    val a = Random.nextInt(10, 30)
    val b = Random.nextInt(2, 10)
    val c = Random.nextInt(5, 20)
    
    return when (Random.nextInt(3)) {
        0 -> {
            // Multiplication then addition: (a × b) + c
            MathProblem("($a × $b) + $c = ?", a * b + c)
        }
        1 -> {
            // Division
            val quotient = Random.nextInt(2, 10)
            val divisor = Random.nextInt(2, 10)
            val dividend = quotient * divisor
            MathProblem("$dividend ÷ $divisor = ?", quotient)
        }
        else -> {
            // Two multiplications: a × b + c × d
            val d = Random.nextInt(2, 8)
            MathProblem("$a × $b + $c × $d = ?", a * b + c * d)
        }
    }
}

private fun generateNightmareProblem(): MathProblem {
    return when (Random.nextInt(3)) {
        0 -> {
            // Complex: (a × b) - (c × d) + e
            val a = Random.nextInt(5, 15)
            val b = Random.nextInt(3, 10)
            val c = Random.nextInt(3, 10)
            val d = Random.nextInt(2, 8)
            val e = Random.nextInt(10, 30)
            MathProblem("($a × $b) - ($c × $d) + $e = ?", a * b - c * d + e)
        }
        1 -> {
            // Square
            val n = Random.nextInt(5, 12)
            MathProblem("$n² = ?", n * n)
        }
        else -> {
            // Multiple operations
            val a = Random.nextInt(10, 20)
            val b = Random.nextInt(2, 5)
            val c = Random.nextInt(2, 5)
            val d = Random.nextInt(10, 20)
            MathProblem("$a × $b + $c × $d = ?", a * b + c * d)
        }
    }
}
