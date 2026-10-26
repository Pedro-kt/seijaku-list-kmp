package com.yumedev.seijakulistkmp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yumedev.seijakulistkmp.core.analytics.AnalyticsManager
import com.yumedev.seijakulistkmp.core.notification.AiringNotificationScheduler
import com.yumedev.seijakulistkmp.di.initKoin
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetAiringNotificationsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

class SeijakuApplication : Application(), KoinComponent {

    private val airingNotificationScheduler: AiringNotificationScheduler by inject()
    private val getAiringNotificationsUseCase: GetAiringNotificationsUseCase by inject()
    private val analyticsManager: AnalyticsManager by inject()
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        initializeCrashlytics()

        initKoin(googleWebClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID) {
            androidLogger()
            androidContext(this@SeijakuApplication)
        }

        initializeAnalytics()
        createNotificationChannel()
        applyLanguage()
        initializeNotifications()
    }

    private fun initializeCrashlytics() {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        crashlytics.setCustomKey("app_version", BuildConfig.VERSION_NAME)
        crashlytics.setCustomKey("version_code", BuildConfig.VERSION_CODE)

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            crashlytics.recordException(throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun initializeAnalytics() {
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)

        analyticsManager.setCustomKey("app_version", BuildConfig.VERSION_NAME)
        analyticsManager.setCustomKey("version_code", BuildConfig.VERSION_CODE)
        analyticsManager.setCustomKey("platform", "Android")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Airing Notifications"
            val descriptionText = "Notifications for anime episodes airing soon"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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

    private fun initializeNotifications() {
        applicationScope.launch {
            try {
                val notificationsEnabled = getAiringNotificationsUseCase().first()
                if (notificationsEnabled) {
                    airingNotificationScheduler.startPeriodicSync()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "airing_notifications"
    }
}
