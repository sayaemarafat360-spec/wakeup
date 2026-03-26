package com.wakeupwarrior.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

// Custom shapes for glassmorphic components
val GlassCardShape = RoundedCornerShape(24.dp)
val GlassButtonShape = RoundedCornerShape(16.dp)
val GlassTextFieldShape = RoundedCornerShape(12.dp)
val GlassChipShape = RoundedCornerShape(20.dp)
val BottomSheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
val DialogShape = RoundedCornerShape(28.dp)
