package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalImageReader = staticCompositionLocalOf<ImageReader> {
    error("ImageReader not provided")
}

@Composable
expect fun rememberImageReader(): ImageReader
