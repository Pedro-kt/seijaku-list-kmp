package com.yumedev.seijakulistkmp.core.clipboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIPasteboard

class IosClipboardManager : ClipboardManager {
    override fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}

@Composable
actual fun rememberClipboardManager(): ClipboardManager {
    return remember { IosClipboardManager() }
}
