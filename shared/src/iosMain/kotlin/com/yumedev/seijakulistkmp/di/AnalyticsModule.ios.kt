package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.core.analytics.AnalyticsManager
import com.yumedev.seijakulistkmp.core.analytics.IosAnalyticsManager
import org.koin.dsl.module

actual val platformAnalyticsModule = module {
    single<AnalyticsManager> {
        IosAnalyticsManager()
    }
}
