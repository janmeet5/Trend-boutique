package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color



// ========================================================
// THE TREND BOUTIQUE — AUTHENTIC BRAND COLOR SYSTEM
// Derived directly from the official brand logo & glowing favicon
// ========================================================

// Dark Mode Foundation (Default)
val DarkBackground = Color(0xFF0C0B10)
val DarkSurface = Color(0xFF14131B)
val DarkCard = Color(0xFF1C1A24)
val DarkCardHover = Color(0xFF252230)
val DarkBorder = Color(0xFF2E293D)
val DarkBorderSubtle = Color(0xFF201D2A)

// Light Mode Foundation (Warm, breathable luxury boutique)
val LightBackground = Color(0xFFFBF9F5)
val LightSurface = Color(0xFFFFFFFF)
val LightCard = Color(0xFFFFFFFF)
val LightCardSoft = Color(0xFFF7F3EC)
val LightBorder = Color(0xFFECE5DA)
val LightBorderSubtle = Color(0xFFF3ECE2)

// Soft Friendly Ambient Tint Containers
val SoftPeachContainer = Color(0xFFFFF2EB)
val SoftAmberContainer = Color(0xFFFEF7E6)
val SoftBlushContainer = Color(0xFFFDF0F0)
val SoftGreenContainer = Color(0xFFEDF8F1)

// The Signature Arch Gradient Colors (from the glowing brand icon)
val GlowCrimson = Color(0xFFB82845)
val GlowRose = Color(0xFFE8506D)
val GlowSunset = Color(0xFFF26D60)
val GlowPeach = Color(0xFFFBA260)
val GlowGold = Color(0xFFFED67D)
val GlowStarWhite = Color(0xFFFFFFFF)

// Heritage Punjab Brand Accents
val BurgundyPrimary = Color(0xFF7B1D28)
val BurgundyDark = Color(0xFF5C1D24)
val BurgundyDeep = Color(0xFF24060A)
val BlushRose = Color(0xFFD98282)
val BlushSoft = Color(0xFFFBECEE)
val WarmGold = Color(0xFFC5A059)
val WarmGoldLight = Color(0xFFF0D597)
val WarmGoldDark = Color(0xFFA17F38)
val GoldContainer = Color(0xFFFAF3E3)

// Text Colors
val TextPrimaryDark = Color(0xFFFDFBF7)
val TextSecondaryDark = Color(0xFFB8B1C4)
val TextMutedDark = Color(0xFF7C7588)

val TextPrimaryLight = Color(0xFF221815)
val TextSecondaryLight = Color(0xFF6B5E59)
val TextMutedLight = Color(0xFF9E918B)

// Status & Indicators
val SuccessGreen = Color(0xFF059669)
val SuccessGreenLight = Color(0xFFE8F5E9)
val StarGold = Color(0xFFF59E0B)
val GoogleBlue = Color(0xFF4285F4)
val WhatsAppGreen = Color(0xFF25D366)

// Pre-defined Signature Brand Gradients
val BrandGlowingGradient = Brush.horizontalGradient(
    colors = listOf(GlowCrimson, GlowSunset, GlowPeach, GlowGold)
)

val BrandGlowVerticalGradient = Brush.verticalGradient(
    colors = listOf(GlowGold, GlowPeach, GlowSunset, GlowCrimson)
)

val AmbientWarmGlow = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFF3EA), Color(0xFFFBF9F5))
)

val BrandAmbientOverlayGradient = Brush.verticalGradient(
    colors = listOf(
        Color.Transparent,
        Color(0x99241618),
        Color(0xEB241618)
    )
)

val GeminiPillBorderBrush = Brush.horizontalGradient(
    colors = listOf(
        GlowPeach.copy(alpha = 0.5f),
        GlowGold.copy(alpha = 0.6f),
        GlowRose.copy(alpha = 0.5f)
    )
)

val SoftAmbientBorderBrush = Brush.horizontalGradient(
    colors = listOf(
        LightBorder,
        GlowPeach.copy(alpha = 0.35f),
        GlowGold.copy(alpha = 0.35f),
        LightBorder
    )
)

fun androidx.compose.ui.Modifier.brandGradientBackground(
    enabled: Boolean,
    fallbackColor: Color = LightCard
): androidx.compose.ui.Modifier =
    if (enabled) this.background(BrandGlowingGradient) else this.background(fallbackColor)

// Legacy Aliases mapped to theme tokens for backward compatibility
val CreamBackground = LightBackground
val CreamSurface = LightSurface
val CreamCard = LightCard
val CharcoalText = TextPrimaryLight
val CharcoalMuted = TextSecondaryLight
val CharcoalLight = TextMutedLight
val BorderSubtle = LightBorder
val BorderGold = WarmGold

