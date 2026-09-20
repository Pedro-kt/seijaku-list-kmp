package com.yumedev.seijakulistkmp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowSizeClass {
    Compact,
    Medium,
    Expanded
}

data class ResponsiveValues(
    val cardWidth: Dp,
    val gridColumns: Int,
    val horizontalPadding: Dp,
    val spacingBetweenCards: Dp,
    val featuredCarouselHeight: Dp
)

object ResponsiveDefaults {
    val compact = ResponsiveValues(
        cardWidth = 120.dp,
        gridColumns = 3,
        horizontalPadding = 16.dp,
        spacingBetweenCards = 12.dp,
        featuredCarouselHeight = 220.dp
    )

    val medium = ResponsiveValues(
        cardWidth = 140.dp,
        gridColumns = 4,
        horizontalPadding = 24.dp,
        spacingBetweenCards = 16.dp,
        featuredCarouselHeight = 260.dp
    )

    val expanded = ResponsiveValues(
        cardWidth = 160.dp,
        gridColumns = 6,
        horizontalPadding = 32.dp,
        spacingBetweenCards = 20.dp,
        featuredCarouselHeight = 300.dp
    )

    fun forWindowSize(windowSize: WindowSizeClass): ResponsiveValues {
        return when (windowSize) {
            WindowSizeClass.Compact -> compact
            WindowSizeClass.Medium -> medium
            WindowSizeClass.Expanded -> expanded
        }
    }
}

val LocalResponsiveValues = staticCompositionLocalOf { ResponsiveDefaults.compact }

@Composable
fun rememberWindowSizeClass(widthDp: Int): WindowSizeClass {
    return when {
        widthDp < 600 -> WindowSizeClass.Compact
        widthDp < 840 -> WindowSizeClass.Medium
        else -> WindowSizeClass.Expanded
    }
}

@Composable
fun ProvideResponsiveValues(
    windowSize: WindowSizeClass,
    content: @Composable () -> Unit
) {
    val responsiveValues = ResponsiveDefaults.forWindowSize(windowSize)
    CompositionLocalProvider(LocalResponsiveValues provides responsiveValues) {
        content()
    }
}

object ResponsiveTheme {
    val values: ResponsiveValues
        @Composable
        get() = LocalResponsiveValues.current
}
