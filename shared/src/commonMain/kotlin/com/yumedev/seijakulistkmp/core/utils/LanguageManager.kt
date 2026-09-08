package com.yumedev.seijakulistkmp.core.utils

expect class LanguageManager() {
    fun applyLanguage(languageCode: String)
    fun getCurrentLanguage(): String
}
