package com.yumedev.seijakulistkmp.core.utils

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun rememberImageColors(
    imageUrl: String?,
    fallbackColor: Color
): State<ImageColors> {
    val context = LocalContext.current
    val imageColors = remember {
        mutableStateOf(ImageColors(dominant = fallbackColor, vibrant = fallbackColor))
    }

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNullOrBlank()) {
            imageColors.value = ImageColors(dominant = fallbackColor, vibrant = fallbackColor)
            return@LaunchedEffect
        }

        try {
            val bitmap = withContext(Dispatchers.IO) {
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .build()

                val imageLoader = SingletonImageLoader.get(context)
                val imageResult = imageLoader.execute(request)
                if (imageResult is SuccessResult) {
                    imageResult.image.toBitmap()
                } else {
                    null
                }
            }

            bitmap?.let {
                val colors = extractImageColors(it, fallbackColor)
                imageColors.value = colors
            } ?: run {
                imageColors.value = ImageColors(dominant = fallbackColor, vibrant = fallbackColor)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            imageColors.value = ImageColors(dominant = fallbackColor, vibrant = fallbackColor)
        }
    }

    return imageColors
}

private fun extractImageColors(bitmap: Bitmap, fallbackColor: Color): ImageColors {
    return try {
        val palette = Palette.from(bitmap).generate()

        val dominantSwatch = palette.dominantSwatch
            ?: palette.vibrantSwatch
            ?: palette.darkVibrantSwatch
            ?: palette.lightVibrantSwatch
            ?: palette.mutedSwatch
            ?: palette.darkMutedSwatch
            ?: palette.lightMutedSwatch

        val vibrantSwatch = palette.vibrantSwatch
            ?: palette.lightVibrantSwatch
            ?: palette.darkVibrantSwatch
            ?: palette.dominantSwatch
            ?: palette.mutedSwatch

        val dominantColor = dominantSwatch?.let { Color(it.rgb) } ?: fallbackColor
        val vibrantColor = vibrantSwatch?.let { Color(it.rgb) } ?: fallbackColor

        ImageColors(dominant = dominantColor, vibrant = vibrantColor)
    } catch (e: Exception) {
        e.printStackTrace()
        ImageColors(dominant = fallbackColor, vibrant = fallbackColor)
    }
}
