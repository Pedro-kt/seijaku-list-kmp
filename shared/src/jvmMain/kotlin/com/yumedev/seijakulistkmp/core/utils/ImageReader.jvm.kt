package com.yumedev.seijakulistkmp.core.utils

actual class ImageReader {
    actual suspend fun readImageBytes(imageUri: String): ByteArray {
        throw NotImplementedError("JVM image reading not yet implemented")
    }
}
