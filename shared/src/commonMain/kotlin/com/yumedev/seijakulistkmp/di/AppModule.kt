package com.yumedev.seijakulistkmp.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Main application module that combines all feature modules
 */
val appModule = module {
    includes(
        coreModule,
        animeModule
    )
}

/**
 * Core module for shared dependencies
 */
val coreModule = module {}
