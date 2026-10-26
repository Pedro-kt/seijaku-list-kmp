package com.yumedev.seijakulistkmp.core.utils

actual class ImageReader {
    actual suspend fun readImageBytes(imageUri: String): ByteArray {
        throw NotImplementedError("iOS image reading not yet implemented")
    }
}
