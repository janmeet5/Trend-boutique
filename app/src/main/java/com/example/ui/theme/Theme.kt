package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BoutiqueDarkColorScheme = darkColorScheme(
    primary = GlowPeach,
    onPrimary = Color(0xFF14131B),
    primaryContainer = BurgundyDark,
    onPrimaryContainer = GlowGold,
    secondary = GlowGold,
    onSecondary = Color(0xFF14131B),
    secondaryContainer = DarkCard,
    onSecondaryContainer = GlowPeach,
    tertiary = GlowSunset,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkBorder,
    outlineVariant = GlowSunset.copy(alpha = 0.4f)
)

private val BoutiqueLightColorScheme = lightColorScheme(
    primary = BurgundyPrimary,
    onPrimary = Color.White,
    primaryContainer = BlushSoft,
    onPrimaryContainer = BurgundyDark,
    secondary = GlowPeach,
    onSecondary = Color(0xFF221815),
    secondaryContainer = SoftPeachContainer,
    onSecondaryContainer = BurgundyPrimary,
    tertiary = GlowSunset,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightCardSoft,
    onSurfaceVariant = TextSecondaryLight,
    outline = LightBorder,
    outlineVariant = LightBorderSubtle
)

@Composable
fun TrendBoutiqueTheme(
    darkTheme: Boolean = false, // Default to warm, breathable Light Mode as requested
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) BoutiqueDarkColorScheme else BoutiqueLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

