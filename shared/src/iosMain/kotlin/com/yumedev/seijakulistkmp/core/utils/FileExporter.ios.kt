package com.yumedev.seijakulistkmp.core.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual class FileExporter {
    actual fun exportXmlFile(
        content: String,
        fileName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val fileManager = NSFileManager.defaultManager
            val tempDir = fileManager.temporaryDirectory.path ?: run {
                onError("Could not access temporary directory")
                return
            }

            val filePath = "$tempDir/$fileName"

            val nsString = NSString.create(string = content)
            val data = nsString.dataUsingEncoding(NSUTF8StringEncoding) ?: run {
                onError("Could not encode file content")
                return
            }

            val writeSuccess = (data as NSData).writeToFile(filePath, atomically = true)
            if (!writeSuccess) {
                onError("Could not write file to disk")
                return
            }

            val fileURL = platform.Foundation.NSURL.fileURLWithPath(filePath)

            val activityViewController = UIActivityViewController(
                activityItems = listOf(fileURL),
                applicationActivities = null
            )

            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            rootViewController?.presentViewController(
                activityViewController,
                animated = true,
                completion = {
                    onSuccess()
                }
            )
        } catch (e: Exception) {
            onError(e.message ?: "Unknown error occurred while exporting file")
        }
    }
}
