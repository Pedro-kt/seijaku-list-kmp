package com.yumedev.seijakulistkmp.core.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberFilePicker(): FilePicker {
    val context = LocalContext.current

    var filePicker: FilePicker? = null

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        filePicker?.handleFileUri(uri)
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        filePicker?.handleImageUri(uri)
    }

    filePicker = remember(context, fileLauncher, imageLauncher) {
        FilePicker(
            context = context,
            launchPicker = { fileLauncher.launch(arrayOf("text/xml", "application/xml")) },
            launchImagePicker = {
                imageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }

    return filePicker
}
