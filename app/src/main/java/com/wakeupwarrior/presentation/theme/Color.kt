package com.wakeupwarrior.presentation.theme

import androidx.compose.ui.graphics.Color

// Primary Colors
val Primary = Color(0xFF6C5CE7)
val PrimaryDark = Color(0xFF5549D4)
val PrimaryLight = Color(0xFF8B7EF0)

// Secondary Colors
val Secondary = Color(0xFF00CEC9)
val SecondaryDark = Color(0xFF00A8A4)
val SecondaryLight = Color(0xFF33E0DC)

// Tertiary Colors
val Tertiary = Color(0xFFFD79A8)
val TertiaryDark = Color(0xFFFC5D94)
val TertiaryLight = Color(0xFFFE94BC)

// Status Colors
val Success = Color(0xFF00B894)
val Warning = Color(0xFFFDCB6E)
val Error = Color(0xFFE17055)
val Info = Color(0xFF74B9FF)

// Background Colors
val Background = Color(0xFF0D0D1A)
val Surface = Color(0xFF1A1A2E)
val SurfaceHigh = Color(0xFF252542)
val SurfaceOverlay = Color(0xFF16162B)

// Glass Effect Colors
val GlassBackground = Color(0x14FFFFFF)
val GlassBackgroundLight = Color(0x1FFFFFFF)
val GlassBorder = Color(0x26FFFFFF)
val GlassBorderHighlight = Color(0x40FFFFFF)

// Text Colors
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB4B4C4)
val TextMuted = Color(0xFF6C6C8A)
val TextDisabled = Color(0xFF4A4A5E)

// Alarm States
val AlarmActive = Color(0xFF00B894)
val AlarmInactive = Color(0xFF6C6C8A)
val AlarmUrgent = Color(0xFFE17055)

// Gradients
val GradientPrimary = listOf(Primary, Secondary)
val GradientAlarm = listOf(Error, Warning)
val GradientSuccess = listOf(Success, Secondary)
val GradientBackground = listOf(Background, SurfaceOverlay)

// Chart Colors
val ChartColors = listOf(
    Primary,
    Secondary,
    Tertiary,
    Success,
    Warning,
    Error
)
