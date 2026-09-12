package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
actual fun rememberImageColors(
    imageUrl: String?,
    fallbackColor: Color
): State<ImageColors> {
    return remember { mutableStateOf(ImageColors(dominant = fallbackColor, vibrant = fallbackColor)) }
}
