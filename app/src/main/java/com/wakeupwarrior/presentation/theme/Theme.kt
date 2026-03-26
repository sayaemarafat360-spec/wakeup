package com.wakeupwarrior.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryLight,
    
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = SecondaryLight,
    
    tertiary = Tertiary,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryDark,
    onTertiaryContainer = TertiaryLight,
    
    error = Error,
    onError = Color.White,
    errorContainer = Error.copy(alpha = 0.2f),
    onErrorContainer = Error,
    
    background = Background,
    onBackground = TextPrimary,
    
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceHigh,
    onSurfaceVariant = TextSecondary,
    
    outline = GlassBorder,
    outlineVariant = TextMuted,
    
    inverseSurface = SurfaceHigh,
    inverseOnSurface = TextPrimary,
    inversePrimary = PrimaryLight
)

@Composable
fun WakeUpWarriorTheme(
    darkTheme: Boolean = true, // Always dark for this app
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable for consistent branding
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
