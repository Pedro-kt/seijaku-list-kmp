package com.yumedev.seijakulistkmp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeVariant
import com.yumedev.seijakulistkmp.ui.theme.colors.OceanColors
import com.yumedev.seijakulistkmp.ui.theme.colors.SakuraColors
import com.yumedev.seijakulistkmp.ui.theme.colors.SumiColors
import com.yumedev.seijakulistkmp.ui.theme.colors.ThemeColors
import com.yumedev.seijakulistkmp.ui.theme.colors.YozoraColors

val LocalExpressiveShapes = staticCompositionLocalOf { expressiveShapes }

private fun getThemeColors(variant: ThemeVariant): ThemeColors {
    return when (variant) {
        ThemeVariant.SUMI -> SumiColors
        ThemeVariant.SAKURA -> SakuraColors
        ThemeVariant.OCEAN -> OceanColors
        ThemeVariant.YOZORA -> YozoraColors
    }
}

@Composable
fun SeijakuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeVariant: ThemeVariant = ThemeVariant.SUMI,
    content: @Composable () -> Unit,
) {
    val themeColors = getThemeColors(themeVariant)
    val colorScheme = if (darkTheme) themeColors.darkColorScheme() else themeColors.lightColorScheme()

    CompositionLocalProvider(LocalExpressiveShapes provides expressiveShapes) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = SeijakuTypography(),
            shapes = material3Shapes,
            content = content,
        )
    }
}

object SeijakuTheme {
    val shapes: ExpressiveShapes
        @Composable
        get() = LocalExpressiveShapes.current
}
