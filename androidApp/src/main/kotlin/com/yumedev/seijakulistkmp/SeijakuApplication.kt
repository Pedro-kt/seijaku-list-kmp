package com.yumedev.seijakulistkmp

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

class SeijakuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        applyLanguage()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyLanguage()
    }

    private fun applyLanguage() {
        val prefsName = "${packageName}_preferences"
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val languageModeName = prefs.getString("language_mode", "SYSTEM") ?: "SYSTEM"

        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].language
        } else {
            @Suppress("DEPRECATION")
            resources.configuration.locale.language
        }

        val languageCode = when (languageModeName) {
            "ENGLISH" -> "en"
            "SPANISH" -> "es"
            "SYSTEM" -> if (systemLocale == "es") "es" else "en"
            else -> if (systemLocale == "es") "es" else "en"
        }

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createConfigurationContext(configuration)
        } else {
            @Suppress("DEPRECATION")
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }
}
