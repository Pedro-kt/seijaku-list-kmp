package com.yumedev.seijakulistkmp.core.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class UrlOpener {
    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        if (UIApplication.sharedApplication.canOpenURL(nsUrl)) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }
}
