package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalFileExporter = staticCompositionLocalOf<FileExporter> {
    error("No FileExporter provided")
}

@Composable
expect fun rememberFileExporter(): FileExporter
