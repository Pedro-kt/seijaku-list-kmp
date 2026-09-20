package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.core.notification.AiringNotificationScheduler
import com.yumedev.seijakulistkmp.core.notification.AndroidAiringNotificationScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformNotificationModule: Module = module {
    single<AiringNotificationScheduler> {
        AndroidAiringNotificationScheduler(androidContext())
    }
}
