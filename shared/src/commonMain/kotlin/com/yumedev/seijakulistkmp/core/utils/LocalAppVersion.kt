package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberAppVersion(): Pair<String, String>
