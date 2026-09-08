package com.yumedev.seijakulistkmp.core.utils

import platform.Foundation.NSBundle

actual class AppVersionProvider {
    actual fun getVersionName(): String {
        val bundle = NSBundle.mainBundle
        return bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0"
    }

    actual fun getVersionCode(): String {
        val bundle = NSBundle.mainBundle
        return bundle.objectForInfoDictionaryKey("CFBundleVersion") as? String ?: "1"
    }
}
