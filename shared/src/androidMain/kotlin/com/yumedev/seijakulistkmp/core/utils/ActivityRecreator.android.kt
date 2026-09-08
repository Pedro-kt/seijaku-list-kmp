package com.yumedev.seijakulistkmp.core.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberActivityRecreator(): ActivityRecreator {
    val activity = LocalContext.current as? Activity
    return remember(activity) {
        object : ActivityRecreator {
            override fun recreate() {
                activity?.recreate()
            }
        }
    }
}
