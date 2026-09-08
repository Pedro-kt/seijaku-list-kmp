package com.yumedev.seijakulistkmp.core.utils

actual class FileExporter {
    actual fun exportXmlFile(
        content: String,
        fileName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // JVM/Desktop implementation not yet supported
        onError("File export not yet implemented for Desktop")
    }
}
