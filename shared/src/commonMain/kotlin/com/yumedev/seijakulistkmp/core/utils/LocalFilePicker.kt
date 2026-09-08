package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalFilePicker = staticCompositionLocalOf<FilePicker> {
    error("No FilePicker provided")
}

@Composable
expect fun rememberFilePicker(): FilePicker
