package com.wakeupwarrior.presentation.screens.challenge.games

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wakeupwarrior.data.model.Difficulty
import com.wakeupwarrior.presentation.components.*
import com.wakeupwarrior.presentation.theme.*

@Composable
fun ShakeChallengeScreen(
    difficulty: Difficulty,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    
    val targetShakes = when (difficulty) {
        Difficulty.EASY -> 10
        Difficulty.MEDIUM -> 20
        Difficulty.HARD -> 35
        Difficulty.NIGHTMARE -> 50
    }
    
    var shakeCount by remember { mutableStateOf(0) }
    var lastAcceleration by remember { mutableFloatStateOf(0f) }
    var lastUpdate by remember { mutableLongStateOf(0L) }
    
    // Animation for shake progress
    val progress by animateFloatAsState(
        targetValue = shakeCount.toFloat() / targetShakes.toFloat(),
        animationSpec = tween(100),
        label = "progress"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (shakeCount >= targetShakes) 1.2f else 1f + (progress * 0.1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    
    // Sensor listener
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    
                    val acceleration = kotlin.math.sqrt(x * x + y * y + z * z)
                    
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastUpdate > 100) {
                        val delta = acceleration - lastAcceleration
                        if (delta > 3f && shakeCount < targetShakes) {
                            shakeCount++
                            // Vibrate feedback
                            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                vibrator.vibrate(android.os.VibrationEffect.createOneShot(50, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
                            }
                        }
                        lastAcceleration = acceleration
                        lastUpdate = currentTime
                    }
                }
            }
            
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }
    
    DisposableEffect(Unit) {
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
    
    // Auto complete when target reached
    LaunchedEffect(shakeCount) {
        if (shakeCount >= targetShakes) {
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
            text = "Shake Challenge",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Shake your phone $targetShakes times",
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
                        text = "📳",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = MaterialTheme.typography.displayLarge.fontSize * 2
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "$shakeCount / $targetShakes",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                    
                    Text(
                        text = "shakes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Instructions
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "How to complete",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextMuted
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Shake your phone vigorously!\nThe more you shake, the closer you get to waking up.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Remaining count
        AnimatedContent(
            targetState = targetShakes - shakeCount,
            label = "remaining"
        ) { remaining ->
            Text(
                text = if (remaining > 0) "$remaining more shakes to go!" else "Complete! 🎉",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (remaining > 0) Primary else Success
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}
