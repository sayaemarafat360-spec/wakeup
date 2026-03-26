package com.wakeupwarrior.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wakeupwarrior.presentation.theme.*

/**
 * A glassmorphic card component with blur-like effect and gradient borders.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = GlassBackground,
    borderColor: Color = GlassBorder,
    cornerRadius: Dp = 24.dp,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )
    
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isPressed) backgroundColor.copy(alpha = backgroundColor.alpha * 1.5f) else backgroundColor,
        animationSpec = tween(100),
        label = "backgroundColor"
    )
    
    val shape = RoundedCornerShape(cornerRadius)
    
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        animatedBackgroundColor,
                        animatedBackgroundColor.copy(alpha = animatedBackgroundColor.alpha * 0.5f)
                    )
                ),
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.6f),
                        borderColor.copy(alpha = 0.2f),
                        borderColor.copy(alpha = 0.4f)
                    )
                ),
                shape = shape
            )
            .padding(1.dp)
    ) {
        content()
    }
}

/**
 * A variant of GlassCard with glow effect.
 */
@Composable
fun GlassCardGlow(
    modifier: Modifier = Modifier,
    glowColor: Color = Primary.copy(alpha = 0.3f),
    backgroundColor: Color = GlassBackground,
    borderColor: Color = GlassBorder,
    cornerRadius: Dp = 24.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    
    Box(
        modifier = modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        this.asFrameworkPaint().setShadowLayer(
                            20f,
                            0f,
                            0f,
                            glowColor
                        )
                    }
                    canvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        cornerRadius.value,
                        cornerRadius.value,
                        paint
                    )
                }
            }
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = backgroundColor.alpha * 0.5f)
                    )
                ),
                shape = shape
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.6f),
                        borderColor.copy(alpha = 0.2f),
                        borderColor.copy(alpha = 0.4f)
                    )
                ),
                shape = shape
            )
            .padding(1.dp)
    ) {
        content()
    }
}

/**
 * A simple glass container without click functionality.
 */
@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    GlassCard(
        modifier = modifier,
        onClick = null,
        content = content
    )
}
