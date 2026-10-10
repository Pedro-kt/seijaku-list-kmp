package com.yumedev.seijakulistkmp.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun rememberNotificationPermissionRequester(): NotificationPermissionRequester {
    val context = LocalContext.current

    val impl = remember {
        NotificationPermissionRequesterImpl(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        impl.handlePermissionResult(isGranted)
    }

    impl.setLauncher { launcher.launch(it) }

    return impl
}

private class NotificationPermissionRequesterImpl(
    private val context: Context
) : NotificationPermissionRequester {
    private var onGrantedCallback: (() -> Unit)? = null
    private var onDeniedCallback: (() -> Unit)? = null
    private var launchRequest: ((String) -> Unit)? = null

    fun setLauncher(launcher: (String) -> Unit) {
        launchRequest = launcher
    }

    override fun requestPermission(
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onGrantedCallback = onGranted
            onDeniedCallback = onDenied
            launchRequest?.invoke(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onGranted()
        }
    }

    override fun checkPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun handlePermissionResult(isGranted: Boolean) {
        if (isGranted) {
            onGrantedCallback?.invoke()
        } else {
            onDeniedCallback?.invoke()
        }
        onGrantedCallback = null
        onDeniedCallback = null
    }
}
