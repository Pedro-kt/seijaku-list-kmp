package com.yumedev.seijakulistkmp.di

import com.russhwolf.settings.Settings
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatterImpl
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    singleOf(::MediaStringFormatterImpl) bind MediaStringFormatter::class

    single {
        Settings()
    }

    single {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}
