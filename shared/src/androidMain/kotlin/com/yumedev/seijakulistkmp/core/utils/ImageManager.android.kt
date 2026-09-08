package com.yumedev.seijakulistkmp.core.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

actual class ImageManager(
    private val context: Context
) {
    actual fun saveImage(
        imageUri: String,
        destinationFileName: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val uri = Uri.parse(imageUri)
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: run {
                    onError("Failed to open image")
                    return
                }

            val imagesDir = File(context.filesDir, "profile_images")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }

            val destinationFile = File(imagesDir, destinationFileName)
            val outputStream = FileOutputStream(destinationFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            onSuccess(destinationFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e.message ?: "Failed to save image")
        }
    }
}
