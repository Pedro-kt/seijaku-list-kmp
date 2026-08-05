package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalUrlOpener = staticCompositionLocalOf<UrlOpener> {
    error("No UrlOpener provided")
}

@Composable
expect fun rememberUrlOpener(): UrlOpener
