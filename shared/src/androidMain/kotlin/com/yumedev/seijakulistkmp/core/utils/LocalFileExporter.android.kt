package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberFileExporter(): FileExporter {
    val context = LocalContext.current
    return remember(context) {
        FileExporter(context)
    }
}
