package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.core.analytics.AnalyticsManager
import com.yumedev.seijakulistkmp.core.analytics.JvmAnalyticsManager
import org.koin.dsl.module

actual val platformAnalyticsModule = module {
    single<AnalyticsManager> {
        JvmAnalyticsManager()
    }
}
