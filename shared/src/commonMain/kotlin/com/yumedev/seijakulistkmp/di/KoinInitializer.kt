package com.yumedev.seijakulistkmp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(googleWebClientId: String, appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            coreModule,
            networkModule,
            platformAuthModule(googleWebClientId),
            platformNotificationModule,
            authModule,
            homeModule,
            searchModule,
            detailModule,
            characterModule,
            settingsModule,
            trackingModule,
            scheduleModule
        )
    }
}

fun initKoin(googleWebClientId: String) = initKoin(googleWebClientId) {}