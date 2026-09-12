package com.yumedev.seijakulistkmp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            coreModule,
            networkModule,
            homeModule,
            searchModule,
            detailModule,
            characterModule,
            settingsModule,
            trackingModule
        )
    }
}

fun initKoin() = initKoin {}