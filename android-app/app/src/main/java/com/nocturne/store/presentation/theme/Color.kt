package com.nocturne.store.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Exact color tokens extracted from Google Stitch's Nocturne Mode & Obsidian Kinetic DESIGN.md.
 */
object NocturneColors {
    // Primary canvases & tonal surfaces
    val ObsidianDeep = Color(0xFF070B19)
    val MidnightSurface = Color(0xFF0A122A)
    val SurfaceContainerLowest = Color(0xFF050D25)
    val SurfaceDrawerNav = Color(0xFF080F24)
    val SurfaceContainerLow = Color(0xFF131A33)
    val SurfaceContainer = Color(0xFF171E37)
    val SurfaceContainerHigh = Color(0xFF212942)
    val SurfaceContainerHighest = Color(0xFF2C344D)
    val SurfaceBright = Color(0xFF313852)

    // Kinetic Orange & Peach accents
    val KineticOrange = Color(0xFFFF5500)
    val PrimaryContainerOrange = Color(0xFFFF6A00)
    val SolarFlare = Color(0xFFFF7A29)
    val PrimaryPeach = Color(0xFFFFB694)
    val OnPrimaryPeach = Color(0xFF571F00)
    val SecondaryPeach = Color(0xFFFFB68B)
    val SecondaryContainer = Color(0xFF9D4900)
    val OnSecondaryContainer = Color(0xFFFFD4BC)

    // Tertiary & Semantic accents
    val TertiaryCyan = Color(0xFF7BD0FF)
    val CyanFlash = Color(0xFF00A4DC)
    val OnTertiary = Color(0xFF00354A)
    val AmberStar = Color(0xFFFBBF24)
    val SuccessGreen = Color(0xFF34D399)
    val ErrorRed = Color(0xFFFFB4AB)
    val OnError = Color(0xFF690005)
    val ErrorContainer = Color(0xFF93000A)
    val OnErrorContainer = Color(0xFFFFDAD6)

    // Typography & Outlines
    val OnSurface = Color(0xFFDBE1FF)
    val PureWhite = Color(0xFFF8FAFC)
    val OnSurfaceVariant = Color(0xFFE2BFB0)
    val MutedSlate = Color(0xFF94A3B8)
    val OutlineWarm = Color(0xFFA98A7D)
    val OutlineVariant = Color(0xFF5A4136)

    // Glass & Borders
    val GlassBorder = Color(0x14FFFFFF) // rgba(255,255,255,0.08)
    val GlassBorderStrong = Color(0x1FFFFFFF) // rgba(255,255,255,0.12)
    val GlassCardFill = Color(0xB816203D)
    val GlassCardFillBottom = Color(0xE00D142A)
    val GlassInteractiveTop = Color(0xA6182344)
    val GlassInteractiveBottom = Color(0xD10F1730)

    val ObsidianCardBrush = Brush.verticalGradient(
        colors = listOf(GlassCardFill, GlassCardFillBottom)
    )

    val ObsidianInteractiveBrush = Brush.verticalGradient(
        colors = listOf(GlassInteractiveTop, GlassInteractiveBottom)
    )

    val KineticOrangeBrush = Brush.horizontalGradient(
        colors = listOf(KineticOrange, Color(0xFFFF6008), SolarFlare)
    )
}
