package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberAppVersion(): Pair<String, String> {
    return remember {
        val provider = AppVersionProvider()
        provider.getVersionName() to provider.getVersionCode()
    }
}
