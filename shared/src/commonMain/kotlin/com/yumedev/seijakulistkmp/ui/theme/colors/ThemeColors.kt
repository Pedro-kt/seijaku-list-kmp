package com.yumedev.seijakulistkmp.ui.theme.colors

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

interface ThemeColors {
    val primaryDark: Color
    val onPrimaryDark: Color
    val primaryContainerDark: Color
    val onPrimaryContainerDark: Color
    val inversePrimaryDark: Color
    val secondaryDark: Color
    val onSecondaryDark: Color
    val secondaryContainerDark: Color
    val onSecondaryContainerDark: Color
    val tertiaryDark: Color
    val onTertiaryDark: Color
    val tertiaryContainerDark: Color
    val onTertiaryContainerDark: Color
    val errorDark: Color
    val onErrorDark: Color
    val errorContainerDark: Color
    val onErrorContainerDark: Color
    val surfaceDark: Color
    val onSurfaceDark: Color
    val surfaceVariantDark: Color
    val onSurfaceVariantDark: Color
    val surfaceContainerLowestDark: Color
    val surfaceContainerLowDark: Color
    val surfaceContainerDark: Color
    val surfaceContainerHighDark: Color
    val surfaceContainerHighestDark: Color
    val outlineDark: Color
    val outlineVariantDark: Color
    val inverseSurfaceDark: Color
    val inverseOnSurfaceDark: Color

    val primaryLight: Color
    val onPrimaryLight: Color
    val primaryContainerLight: Color
    val onPrimaryContainerLight: Color
    val inversePrimaryLight: Color
    val secondaryLight: Color
    val onSecondaryLight: Color
    val secondaryContainerLight: Color
    val onSecondaryContainerLight: Color
    val tertiaryLight: Color
    val onTertiaryLight: Color
    val tertiaryContainerLight: Color
    val onTertiaryContainerLight: Color
    val errorLight: Color
    val onErrorLight: Color
    val errorContainerLight: Color
    val onErrorContainerLight: Color
    val surfaceLight: Color
    val onSurfaceLight: Color
    val surfaceVariantLight: Color
    val onSurfaceVariantLight: Color
    val surfaceContainerLowestLight: Color
    val surfaceContainerLowLight: Color
    val surfaceContainerLight: Color
    val surfaceContainerHighLight: Color
    val surfaceContainerHighestLight: Color
    val outlineLight: Color
    val outlineVariantLight: Color
    val inverseSurfaceLight: Color
    val inverseOnSurfaceLight: Color

    fun darkColorScheme(): ColorScheme = darkColorScheme(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerDark,
        onPrimaryContainer = onPrimaryContainerDark,
        inversePrimary = inversePrimaryDark,
        secondary = secondaryDark,
        onSecondary = onSecondaryDark,
        secondaryContainer = secondaryContainerDark,
        onSecondaryContainer = onSecondaryContainerDark,
        tertiary = tertiaryDark,
        onTertiary = onTertiaryDark,
        tertiaryContainer = tertiaryContainerDark,
        onTertiaryContainer = onTertiaryContainerDark,
        error = errorDark,
        onError = onErrorDark,
        errorContainer = errorContainerDark,
        onErrorContainer = onErrorContainerDark,
        background = surfaceDark,
        onBackground = onSurfaceDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
        surfaceVariant = surfaceVariantDark,
        onSurfaceVariant = onSurfaceVariantDark,
        surfaceContainerLowest = surfaceContainerLowestDark,
        surfaceContainerLow = surfaceContainerLowDark,
        surfaceContainer = surfaceContainerDark,
        surfaceContainerHigh = surfaceContainerHighDark,
        surfaceContainerHighest = surfaceContainerHighestDark,
        outline = outlineDark,
        outlineVariant = outlineVariantDark,
        inverseSurface = inverseSurfaceDark,
        inverseOnSurface = inverseOnSurfaceDark,
        surfaceTint = primaryDark,
        scrim = Color.Black
    )

    fun lightColorScheme(): ColorScheme = lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryContainerLight,
        inversePrimary = inversePrimaryLight,
        secondary = secondaryLight,
        onSecondary = onSecondaryLight,
        secondaryContainer = secondaryContainerLight,
        onSecondaryContainer = onSecondaryContainerLight,
        tertiary = tertiaryLight,
        onTertiary = onTertiaryLight,
        tertiaryContainer = tertiaryContainerLight,
        onTertiaryContainer = onTertiaryContainerLight,
        error = errorLight,
        onError = onErrorLight,
        errorContainer = errorContainerLight,
        onErrorContainer = onErrorContainerLight,
        background = surfaceLight,
        onBackground = onSurfaceLight,
        surface = surfaceLight,
        onSurface = onSurfaceLight,
        surfaceVariant = surfaceVariantLight,
        onSurfaceVariant = onSurfaceVariantLight,
        surfaceContainerLowest = surfaceContainerLowestLight,
        surfaceContainerLow = surfaceContainerLowLight,
        surfaceContainer = surfaceContainerLight,
        surfaceContainerHigh = surfaceContainerHighLight,
        surfaceContainerHighest = surfaceContainerHighestLight,
        outline = outlineLight,
        outlineVariant = outlineVariantLight,
        inverseSurface = inverseSurfaceLight,
        inverseOnSurface = inverseOnSurfaceLight,
        surfaceTint = primaryLight,
        scrim = Color.Black
    )
}
