package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberFileExporter(): FileExporter {
    return remember {
        FileExporter()
    }
}
