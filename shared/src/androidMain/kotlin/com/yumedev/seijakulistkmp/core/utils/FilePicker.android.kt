package com.yumedev.seijakulistkmp.core.utils

import android.content.Context

actual class FilePicker(
    private val context: Context,
    private val launchPicker: () -> Unit,
    private val launchImagePicker: () -> Unit = {},
    internal var onFileSelectedCallback: ((String) -> Unit)? = null,
    internal var onImageSelectedCallback: ((String) -> Unit)? = null,
    internal var onErrorCallback: ((String) -> Unit)? = null
) {
    actual fun pickXmlFile(
        onFileSelected: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        onFileSelectedCallback = onFileSelected
        onErrorCallback = onError
        try {
            launchPicker()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e.message ?: "Failed to open file picker")
        }
    }

    actual fun pickImage(
        onImageSelected: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        onImageSelectedCallback = onImageSelected
        onErrorCallback = onError
        try {
            launchImagePicker()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e.message ?: "Failed to open image picker")
        }
    }

    fun handleFileUri(uri: android.net.Uri?) {
        if (uri == null) {
            onErrorCallback?.invoke("No file selected")
            return
        }

        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val content = inputStream?.bufferedReader()?.use { it.readText() }

            if (content != null) {
                onFileSelectedCallback?.invoke(content)
            } else {
                onErrorCallback?.invoke("Failed to read file content")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onErrorCallback?.invoke(e.message ?: "Failed to read file")
        }
    }

    fun handleImageUri(uri: android.net.Uri?) {
        if (uri == null) {
            onErrorCallback?.invoke("No image selected")
            return
        }

        try {
            onImageSelectedCallback?.invoke(uri.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            onErrorCallback?.invoke(e.message ?: "Failed to process image")
        }
    }
}
