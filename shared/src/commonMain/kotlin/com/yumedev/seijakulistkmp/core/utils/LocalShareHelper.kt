package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalShareHelper = staticCompositionLocalOf<ShareHelper> {
    error("No ShareHelper provided")
}

@Composable
expect fun rememberShareHelper(): ShareHelper
