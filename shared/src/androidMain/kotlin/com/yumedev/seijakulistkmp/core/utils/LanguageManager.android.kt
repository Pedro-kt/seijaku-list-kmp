package com.yumedev.seijakulistkmp.core.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.ui.text.intl.Locale
import java.util.Locale as JavaLocale

actual class LanguageManager {
    actual fun applyLanguage(languageCode: String) {
    }

    actual fun getCurrentLanguage(): String {
        return Locale.current.language
    }
}
