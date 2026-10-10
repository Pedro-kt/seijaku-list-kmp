package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable

@Composable
actual fun rememberNotificationPermissionRequester(): NotificationPermissionRequester {
    return object : NotificationPermissionRequester {
        override fun requestPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
            onGranted()
        }

        override fun checkPermissionGranted(): Boolean = true
    }
}
