package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable

@Composable
actual fun rememberActivityRecreator(): ActivityRecreator {
    return object : ActivityRecreator {
        override fun recreate() {
        }
    }
}
