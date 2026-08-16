package com.yumedev.seijakulistkmp.core.clipboard

import android.content.ClipData
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class AndroidClipboardManager(private val context: Context) : ClipboardManager {
    override fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(clip)
    }
}

@Composable
actual fun rememberClipboardManager(): ClipboardManager {
    val context = LocalContext.current
    return remember { AndroidClipboardManager(context) }
}
