package com.yumedev.seijakulistkmp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            coreModule,
            networkModule,
            animeModule,
            homeModule
        )
    }
}

fun initKoin() = initKoin {}