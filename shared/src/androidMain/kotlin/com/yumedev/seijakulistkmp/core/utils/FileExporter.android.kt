package com.yumedev.seijakulistkmp.core.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

actual class FileExporter(private val context: Context) {
    actual fun exportXmlFile(
        content: String,
        fileName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val cacheDir = context.cacheDir
            val file = File(cacheDir, fileName)

            file.writeText(content)

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/xml"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, fileName)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val resInfoList = context.packageManager.queryIntentActivities(shareIntent, 0)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Export $fileName").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(chooserIntent)
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e.message ?: "Unknown error occurred while exporting file")
        }
    }
}
