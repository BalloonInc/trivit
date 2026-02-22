package com.wouterdevriendt.trivit.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = TrivitColors.Turquoise,
    onPrimary = Color.White,
    primaryContainer = TrivitColors.Turquoise.copy(alpha = 0.15f),
    onPrimaryContainer = TrivitColors.TurquoiseDark,
    secondary = TrivitColors.PeterRiver,
    onSecondary = Color.White,
    secondaryContainer = TrivitColors.PeterRiver.copy(alpha = 0.15f),
    onSecondaryContainer = TrivitColors.PeterRiverDark,
    tertiary = TrivitColors.Amethyst,
    onTertiary = Color.White,
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = Color(0xFF44474F),
    outline = Color(0xFFE0E0E0)
)

private val DarkColorScheme = darkColorScheme(
    primary = TrivitColors.Turquoise,
    onPrimary = Color.White,
    primaryContainer = TrivitColors.TurquoiseDark,
    onPrimaryContainer = Color.White,
    secondary = TrivitColors.PeterRiver,
    onSecondary = Color.White,
    secondaryContainer = TrivitColors.PeterRiverDark,
    onSecondaryContainer = Color.White,
    tertiary = TrivitColors.Amethyst,
    onTertiary = Color.White,
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF2C2F33),
    onSurfaceVariant = Color(0xFFC4C6D0),
    outline = Color(0xFF3E4145)
)

@Composable
fun TrivitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
