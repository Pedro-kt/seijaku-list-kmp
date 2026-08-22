package com.yumedev.seijakulistkmp.core.utils

expect class FileExporter {
    fun exportXmlFile(
        content: String,
        fileName: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    )
}
