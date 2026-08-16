package com.yumedev.seijakulistkmp.core.clipboard

import androidx.compose.runtime.Composable

interface ClipboardManager {
    fun copyToClipboard(text: String)
}

@Composable
expect fun rememberClipboardManager(): ClipboardManager
