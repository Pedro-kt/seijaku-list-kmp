package com.yumedev.seijakulistkmp.core.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build

actual class AppVersionProvider {
    actual fun getVersionName(): String {
        return "1.0"
    }

    actual fun getVersionCode(): String {
        return "1"
    }
}

fun AppVersionProvider.getVersionInfo(context: Context): Pair<String, String> {
    return try {
        val packageInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                android.content.pm.PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }

        val versionName = packageInfo.versionName ?: "Unknown"
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toString()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toString()
        }

        versionName to versionCode
    } catch (e: Exception) {
        "Unknown" to "0"
    }
}
