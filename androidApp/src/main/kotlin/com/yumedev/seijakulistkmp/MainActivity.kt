package com.yumedev.seijakulistkmp

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yumedev.seijakulistkmp.features.tracking.data.local.TrackingDatabaseBuilder
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        TrackingDatabaseBuilder.initialize(applicationContext)

        setContent {
            App()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(applyLanguage(newBase))
    }

    private fun applyLanguage(context: Context): Context {
        val prefsName = "${context.packageName}_preferences"
        val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val languageModeName = prefs.getString("language_mode", "SYSTEM") ?: "SYSTEM"

        println("MainActivity - SharedPreferences name: $prefsName")
        println("MainActivity - Language mode from prefs: $languageModeName")
        println("MainActivity - All prefs: ${prefs.all}")

        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale.language
        }

        println("MainActivity - System locale: $systemLocale")

        val languageCode = when (languageModeName) {
            "ENGLISH" -> "en"
            "SPANISH" -> "es"
            "SYSTEM" -> if (systemLocale == "es") "es" else "en"
            else -> if (systemLocale == "es") "es" else "en"
        }

        println("MainActivity - Applying language code: $languageCode")

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
