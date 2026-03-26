package com.wakeupwarrior.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import com.wakeupwarrior.presentation.theme.*

/**
 * Animated gradient background for screens.
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Background,
                        SurfaceOverlay,
                        Background.copy(alpha = 0.8f),
                        SurfaceOverlay
                    ),
                    start = Offset(offset, offset),
                    end = Offset(offset + 1000f, offset + 1000f)
                )
            )
    ) {
        content()
    }
}

/**
 * Gradient background with floating orbs effect.
 */
@Composable
fun GradientBackgroundWithOrbs(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")
    
    val orbOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1"
    )
    
    val orbOffset2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Gradient orbs
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    // Primary orb
                    drawCircle(
                        color = Primary.copy(alpha = 0.15f),
                        radius = size.width * 0.4f,
                        center = Offset(
                            size.width * 0.2f + (size.width * 0.3f * orbOffset1),
                            size.height * 0.3f
                        )
                    )
                    
                    // Secondary orb
                    drawCircle(
                        color = Secondary.copy(alpha = 0.1f),
                        radius = size.width * 0.35f,
                        center = Offset(
                            size.width * 0.8f - (size.width * 0.3f * orbOffset2),
                            size.height * 0.7f
                        )
                    )
                    
                    // Tertiary orb
                    drawCircle(
                        color = Tertiary.copy(alpha = 0.08f),
                        radius = size.width * 0.25f,
                        center = Offset(
                            size.width * 0.5f,
                            size.height * 0.5f + (size.height * 0.2f * orbOffset1)
                        )
                    )
                }
        )
        
        content()
    }
}

/**
 * Alarm ringing background with pulsing effect.
 */
@Composable
fun AlarmPulseBackground(
    modifier: Modifier = Modifier,
    pulseColor: Color = Error.copy(alpha = 0.3f),
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .drawBehind {
                drawCircle(
                    color = pulseColor.copy(alpha = pulseAlpha),
                    radius = size.minDimension * pulseScale,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            }
    ) {
        content()
    }
}
