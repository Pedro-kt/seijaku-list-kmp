package com.yumedev.seijakulistkmp.core.utils

import androidx.compose.ui.text.intl.Locale

actual class LanguageManager {
    actual fun applyLanguage(languageCode: String) {
    }

    actual fun getCurrentLanguage(): String {
        return Locale.current.language
    }
}
