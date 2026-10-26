package com.yumedev.seijakulistkmp.core.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

actual class ImageReader(private val context: Context) {
    actual suspend fun readImageBytes(imageUri: String): ByteArray = withContext(Dispatchers.IO) {
        val uri = Uri.parse(imageUri)
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Failed to open image stream")

        val buffer = ByteArrayOutputStream()
        inputStream.use { input ->
            val data = ByteArray(8192)
            var bytesRead: Int
            while (input.read(data).also { bytesRead = it } != -1) {
                buffer.write(data, 0, bytesRead)
            }
        }

        buffer.toByteArray()
    }
}
