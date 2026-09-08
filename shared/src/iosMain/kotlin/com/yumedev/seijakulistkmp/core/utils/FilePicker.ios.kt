package com.yumedev.seijakulistkmp.core.utils

actual class FilePicker {
    actual fun pickXmlFile(
        onFileSelected: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        onError("File picker not implemented for iOS yet")
    }

    actual fun pickImage(
        onImageSelected: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        onError("Image picker not implemented for iOS yet")
    }
}
