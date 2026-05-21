package com.bistpicker.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.bistpicker.mobile.data.AppearanceMode

// Material 3 color schemes derived from res/values/colors.xml palette.
// We deliberately do NOT use Material You / dynamic color (Android 12+
// would otherwise recolor the app from the user's wallpaper). Stocks live
// or die by P&L green/red; recoloring chrome from the user's wallpaper
// damages that recognition.

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFFFB48A),
    onPrimary = Color(0xFF2A1B0A),
    primaryContainer = Color(0xFF3D2A1A),
    onPrimaryContainer = Color(0xFFFFE0CB),

    secondary = Color(0xFF7DD3FC),
    onSecondary = Color(0xFF002C44),
    secondaryContainer = Color(0xFF003B5C),
    onSecondaryContainer = Color(0xFFCAEAFF),

    tertiary = Color(0xFFA78BFA),
    onTertiary = Color(0xFF1F0F45),
    tertiaryContainer = Color(0xFF341E66),
    onTertiaryContainer = Color(0xFFEADCFF),

    background = Color(0xFF0E0E10),
    onBackground = Color(0xFFF5F5F4),
    surface = Color(0xFF141416),
    onSurface = Color(0xFFF5F5F4),
    surfaceVariant = Color(0xFF1F1F23),
    onSurfaceVariant = Color(0xFFB5B5B2),
    surfaceContainer = Color(0xFF1F1F23),
    surfaceContainerHigh = Color(0xFF26262B),

    outline = Color(0xFF3A3A3F),
    outlineVariant = Color(0xFF26262B),

    error = Color(0xFFEF4444),
    onError = Color(0xFF400000),
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF8B4F1E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFE0CB),
    onPrimaryContainer = Color(0xFF2A1B0A),

    secondary = Color(0xFF005684),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCAEAFF),

    tertiary = Color(0xFF5E3DBF),
    onTertiary = Color(0xFFFFFFFF),

    background = Color(0xFFFAFAF9),
    onBackground = Color(0xFF191919),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF191919),
    surfaceVariant = Color(0xFFF1F1EE),
    onSurfaceVariant = Color(0xFF525252),

    outline = Color(0xFFD4D4D2),
    outlineVariant = Color(0xFFE7E5E4),

    error = Color(0xFFB00020),
)

@Composable
fun BistPickerTheme(
    appearance: AppearanceMode = AppearanceMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (appearance) {
        AppearanceMode.DARK -> true
        AppearanceMode.LIGHT -> false
        AppearanceMode.SYSTEM -> systemDark
    }
    MaterialTheme(
        colorScheme = if (isDark) DarkScheme else LightScheme,
        typography = BistTypography,
        content = content,
    )
}
