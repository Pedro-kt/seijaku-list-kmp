package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberImageManager(): ImageManager {
    return remember {
        ImageManager()
    }
}
