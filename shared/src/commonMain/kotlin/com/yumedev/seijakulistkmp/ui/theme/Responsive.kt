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
    val smallPadding: Dp,
    val mediumPadding: Dp,
    val largePadding: Dp,
    val xlargePadding: Dp,
    val spacingBetweenCards: Dp,
    val smallSpacing: Dp,
    val mediumSpacing: Dp,
    val largeSpacing: Dp,
    val featuredCarouselHeight: Dp,
    val buttonHeight: Dp,
    val smallButtonHeight: Dp,
    val mediumButtonHeight: Dp,
    val largeButtonHeight: Dp,
    val iconSize: Dp,
    val smallIconSize: Dp,
    val mediumIconSize: Dp,
    val largeIconSize: Dp,
    val chipPadding: Dp,
    val cornerRadius: Dp,
    val minTouchTarget: Dp
)

object ResponsiveDefaults {
    val compact = ResponsiveValues(
        cardWidth = 110.dp,
        cardImageHeight = 155.dp,
        gridColumns = 3,
        horizontalPadding = 12.dp,
        verticalPadding = 12.dp,
        smallPadding = 4.dp,
        mediumPadding = 8.dp,
        largePadding = 16.dp,
        xlargePadding = 24.dp,
        spacingBetweenCards = 8.dp,
        smallSpacing = 4.dp,
        mediumSpacing = 8.dp,
        largeSpacing = 16.dp,
        featuredCarouselHeight = 200.dp,
        buttonHeight = 48.dp,
        smallButtonHeight = 40.dp,
        mediumButtonHeight = 48.dp,
        largeButtonHeight = 56.dp,
        iconSize = 20.dp,
        smallIconSize = 16.dp,
        mediumIconSize = 24.dp,
        largeIconSize = 48.dp,
        chipPadding = 4.dp,
        cornerRadius = 12.dp,
        minTouchTarget = 48.dp
    )

    val medium = ResponsiveValues(
        cardWidth = 130.dp,
        cardImageHeight = 180.dp,
        gridColumns = 4,
        horizontalPadding = 20.dp,
        verticalPadding = 16.dp,
        smallPadding = 6.dp,
        mediumPadding = 12.dp,
        largePadding = 20.dp,
        xlargePadding = 32.dp,
        spacingBetweenCards = 12.dp,
        smallSpacing = 6.dp,
        mediumSpacing = 12.dp,
        largeSpacing = 20.dp,
        featuredCarouselHeight = 240.dp,
        buttonHeight = 52.dp,
        smallButtonHeight = 44.dp,
        mediumButtonHeight = 52.dp,
        largeButtonHeight = 60.dp,
        iconSize = 24.dp,
        smallIconSize = 20.dp,
        mediumIconSize = 28.dp,
        largeIconSize = 56.dp,
        chipPadding = 6.dp,
        cornerRadius = 14.dp,
        minTouchTarget = 48.dp
    )

    val expanded = ResponsiveValues(
        cardWidth = 150.dp,
        cardImageHeight = 200.dp,
        gridColumns = 6,
        horizontalPadding = 32.dp,
        verticalPadding = 24.dp,
        smallPadding = 8.dp,
        mediumPadding = 16.dp,
        largePadding = 24.dp,
        xlargePadding = 40.dp,
        spacingBetweenCards = 16.dp,
        smallSpacing = 8.dp,
        mediumSpacing = 16.dp,
        largeSpacing = 24.dp,
        featuredCarouselHeight = 300.dp,
        buttonHeight = 56.dp,
        smallButtonHeight = 48.dp,
        mediumButtonHeight = 56.dp,
        largeButtonHeight = 64.dp,
        iconSize = 28.dp,
        smallIconSize = 24.dp,
        mediumIconSize = 32.dp,
        largeIconSize = 64.dp,
        chipPadding = 8.dp,
        cornerRadius = 16.dp,
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
