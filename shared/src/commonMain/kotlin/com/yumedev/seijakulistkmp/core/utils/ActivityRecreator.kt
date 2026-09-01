package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberActivityRecreator(): ActivityRecreator

interface ActivityRecreator {
    fun recreate()
}
