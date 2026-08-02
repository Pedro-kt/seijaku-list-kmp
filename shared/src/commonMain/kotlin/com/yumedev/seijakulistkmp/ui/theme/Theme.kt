package com.yumedev.seijakulistkmp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SumiDark = darkColorScheme(
    primary = SumiPrimaryDark,
    onPrimary = SumiOnPrimaryDark,
    primaryContainer = SumiPrimaryContainerDark,
    onPrimaryContainer = SumiOnPrimaryContainerDark,
    inversePrimary = SumiInversePrimaryDark,
    secondary = SumiSecondaryDark,
    onSecondary = SumiOnSecondaryDark,
    secondaryContainer = SumiSecondaryContainerDark,
    onSecondaryContainer = SumiOnSecondaryContainerDark,
    tertiary = SumiTertiaryDark,
    onTertiary = SumiOnTertiaryDark,
    tertiaryContainer = SumiTertiaryContainerDark,
    onTertiaryContainer = SumiOnTertiaryContainerDark,
    error = SumiErrorDark,
    onError = SumiOnErrorDark,
    errorContainer = SumiErrorContainerDark,
    onErrorContainer = SumiOnErrorContainerDark,
    background = SumiSurfaceDark,
    onBackground = SumiOnSurfaceDark,
    surface = SumiSurfaceDark,
    onSurface = SumiOnSurfaceDark,
    surfaceVariant = SumiSurfaceVariantDark,
    onSurfaceVariant = SumiOnSurfaceVariantDark,
    surfaceContainerLowest = SumiSurfaceContLowestDark,
    surfaceContainerLow = SumiSurfaceContLowDark,
    surfaceContainer = SumiSurfaceContDark,
    surfaceContainerHigh = SumiSurfaceContHighDark,
    surfaceContainerHighest = SumiSurfaceContHighestDark,
    outline = SumiOutlineDark,
    outlineVariant = SumiOutlineVariantDark,
    inverseSurface = SumiInverseSurfaceDark,
    inverseOnSurface = SumiInverseOnSurfaceDark,
    surfaceTint = SumiPrimaryDark,
    scrim = Color.Black,
)

private val SumiLight = lightColorScheme(
    primary = SumiPrimaryLight,
    onPrimary = SumiOnPrimaryLight,
    primaryContainer = SumiPrimaryContainerLight,
    onPrimaryContainer = SumiOnPrimaryContainerLight,
    inversePrimary = SumiInversePrimaryLight,
    secondary = SumiSecondaryLight,
    onSecondary = SumiOnSecondaryLight,
    secondaryContainer = SumiSecondaryContainerLight,
    onSecondaryContainer = SumiOnSecondaryContainerLight,
    tertiary = SumiTertiaryLight,
    onTertiary = SumiOnTertiaryLight,
    tertiaryContainer = SumiTertiaryContainerLight,
    onTertiaryContainer = SumiOnTertiaryContainerLight,
    error = SumiErrorLight,
    onError = SumiOnErrorLight,
    errorContainer = SumiErrorContainerLight,
    onErrorContainer = SumiOnErrorContainerLight,
    background = SumiSurfaceLight,
    onBackground = SumiOnSurfaceLight,
    surface = SumiSurfaceLight,
    onSurface = SumiOnSurfaceLight,
    surfaceVariant = SumiSurfaceVariantLight,
    onSurfaceVariant = SumiOnSurfaceVariantLight,
    surfaceContainerLowest = SumiSurfaceContLowestLight,
    surfaceContainerLow = SumiSurfaceContLowLight,
    surfaceContainer = SumiSurfaceContLight,
    surfaceContainerHigh = SumiSurfaceContHighLight,
    surfaceContainerHighest = SumiSurfaceContHighestLight,
    outline = SumiOutlineLight,
    outlineVariant = SumiOutlineVariantLight,
    inverseSurface = SumiInverseSurfaceLight,
    inverseOnSurface = SumiInverseOnSurfaceLight,
    surfaceTint = SumiPrimaryLight,
    scrim = Color.Black,
)

@Composable
fun SeijakuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) SumiDark else SumiLight,
        content = content,
    )
}
