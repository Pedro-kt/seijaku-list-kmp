package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.core.analytics.AnalyticsManager
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformAnalyticsModule: Module

val analyticsModule = module {
    includes(platformAnalyticsModule)
}
