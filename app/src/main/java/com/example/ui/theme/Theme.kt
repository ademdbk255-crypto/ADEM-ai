package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AdemCyanPrimary,
    onPrimary = Color(0xFF00363F),
    primaryContainer = AdemCyanContainer,
    onPrimaryContainer = AdemOnCyanContainer,
    secondary = AdemIndigoSecondary,
    onSecondary = Color(0xFF1E214F),
    secondaryContainer = AdemIndigoContainer,
    onSecondaryContainer = AdemOnIndigoContainer,
    tertiary = AdemPurpleTertiary,
    onTertiary = Color(0xFF4A0072),
    tertiaryContainer = AdemPurpleContainer,
    onTertiaryContainer = Color(0xFFF3E5F5),
    background = AdemDarkBackground,
    onBackground = AdemTextPrimary,
    surface = AdemDarkSurface,
    onSurface = AdemTextPrimary,
    surfaceVariant = AdemDarkSurfaceVariant,
    onSurfaceVariant = AdemTextSecondary,
    outline = AdemBorderSubtle,
    outlineVariant = Color(0xFF263346),
    error = AdemErrorRed,
    onError = Color.White
)

private val LightColorScheme = darkColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF6366F1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = Color(0xFF4338CA),
    tertiary = Color(0xFF9333EA),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFAF5FF),
    onTertiaryContainer = Color(0xFF6B21A8),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    error = Color(0xFFDC2626),
    onError = Color.White
)

@Composable
fun AdemAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We provide a rich, immersive dark theme as the hallmark of ADEM ai
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
