package com.yumedev.seijakulistkmp.core.utils

interface NotificationPermissionRequester {
    fun requestPermission(
        onGranted: () -> Unit,
        onDenied: () -> Unit
    )

    fun checkPermissionGranted(): Boolean
}
