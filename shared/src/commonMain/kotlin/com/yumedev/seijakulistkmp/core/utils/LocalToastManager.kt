package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalToastManager = staticCompositionLocalOf<ToastManager> {
    error("ToastManager not provided")
}

@Composable
expect fun rememberToastManager(): ToastManager
