package com.yumedev.seijakulistkmp.core.utils

actual class ImageManager {
    actual fun saveImage(
        imageUri: String,
        destinationFileName: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        onError("Image manager not implemented for iOS yet")
    }
}
