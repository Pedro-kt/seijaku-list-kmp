package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

data class ImageColors(
    val dominant: Color,
    val vibrant: Color
)

@Composable
expect fun rememberImageColors(
    imageUrl: String?,
    fallbackColor: Color
): State<ImageColors>
