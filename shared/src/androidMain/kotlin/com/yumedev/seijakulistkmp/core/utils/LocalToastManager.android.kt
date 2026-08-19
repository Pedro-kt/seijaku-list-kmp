package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberToastManager(): ToastManager {
    val context = LocalContext.current
    return remember(context) {
        ToastManager(context)
    }
}
