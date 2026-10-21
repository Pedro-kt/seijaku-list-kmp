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
    val cardImageHeight: Dp,
    val gridColumns: Int,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val spacingBetweenCards: Dp,
    val featuredCarouselHeight: Dp,
    val buttonHeight: Dp,
    val iconSize: Dp,
    val chipPadding: Dp,
    val minTouchTarget: Dp
)

object ResponsiveDefaults {
    val compact = ResponsiveValues(
        cardWidth = 110.dp,
        cardImageHeight = 155.dp,
        gridColumns = 3,
        horizontalPadding = 12.dp,
        verticalPadding = 12.dp,
        spacingBetweenCards = 8.dp,
        featuredCarouselHeight = 200.dp,
        buttonHeight = 48.dp,
        iconSize = 20.dp,
        chipPadding = 4.dp,
        minTouchTarget = 48.dp
    )

    val medium = ResponsiveValues(
        cardWidth = 130.dp,
        cardImageHeight = 180.dp,
        gridColumns = 4,
        horizontalPadding = 20.dp,
        verticalPadding = 16.dp,
        spacingBetweenCards = 12.dp,
        featuredCarouselHeight = 240.dp,
        buttonHeight = 52.dp,
        iconSize = 24.dp,
        chipPadding = 6.dp,
        minTouchTarget = 48.dp
    )

    val expanded = ResponsiveValues(
        cardWidth = 150.dp,
        cardImageHeight = 200.dp,
        gridColumns = 6,
        horizontalPadding = 32.dp,
        verticalPadding = 24.dp,
        spacingBetweenCards = 16.dp,
        featuredCarouselHeight = 300.dp,
        buttonHeight = 56.dp,
        iconSize = 28.dp,
        chipPadding = 8.dp,
        minTouchTarget = 56.dp
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
