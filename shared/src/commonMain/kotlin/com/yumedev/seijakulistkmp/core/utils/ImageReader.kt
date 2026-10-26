package com.yumedev.seijakulistkmp.core.utils

expect class ImageReader {
    suspend fun readImageBytes(imageUri: String): ByteArray
}
