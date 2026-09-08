package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.ui.text.intl.Locale

actual class LocaleProvider {
    actual fun getSystemLocale(): String {
        return Locale.current.language
    }
}
