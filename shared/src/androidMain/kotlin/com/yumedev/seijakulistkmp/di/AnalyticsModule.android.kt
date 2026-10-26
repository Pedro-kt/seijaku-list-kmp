package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.core.analytics.AnalyticsManager
import com.yumedev.seijakulistkmp.core.analytics.AndroidAnalyticsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformAnalyticsModule = module {
    single<AnalyticsManager> {
        AndroidAnalyticsManager(androidContext())
    }
}
