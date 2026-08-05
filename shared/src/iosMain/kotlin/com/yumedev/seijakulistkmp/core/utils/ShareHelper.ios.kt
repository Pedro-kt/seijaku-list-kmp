package com.yumedev.seijakulistkmp.core.utils

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.Foundation.NSURL

actual class ShareHelper {
    actual fun shareText(text: String, title: String?) {
        val activityItems = mutableListOf<Any>(text)

        val activityViewController = UIActivityViewController(
            activityItems = activityItems,
            applicationActivities = null
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }
}
