package com.yumedev.seijakulistkmp.core.utils

actual class AppVersionProvider {
    actual fun getVersionName(): String {
        return try {
            val version = this::class.java.`package`?.implementationVersion
            version ?: "1.0"
        } catch (e: Exception) {
            "1.0"
        }
    }

    actual fun getVersionCode(): String {
        return try {
            val buildNumber = this::class.java.`package`?.specificationVersion
            buildNumber ?: "1"
        } catch (e: Exception) {
            "1"
        }
    }
}
