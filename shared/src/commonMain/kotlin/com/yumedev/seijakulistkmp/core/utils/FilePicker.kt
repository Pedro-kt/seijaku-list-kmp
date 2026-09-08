package com.yumedev.seijakulistkmp.core.utils

expect class FilePicker {
    fun pickXmlFile(
        onFileSelected: (String) -> Unit,
        onError: (String) -> Unit = {}
    )

    fun pickImage(
        onImageSelected: (String) -> Unit,
        onError: (String) -> Unit = {}
    )
}
