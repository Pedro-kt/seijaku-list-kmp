package com.yumedev.seijakulistkmp.core.utils

expect class ImageManager {
    fun saveImage(
        imageUri: String,
        destinationFileName: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    )
}
