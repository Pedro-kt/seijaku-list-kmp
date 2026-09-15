package com.yumedev.seijakulistkmp.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformAuthModule(googleWebClientId: String): Module = module {
    // TODO: Implement iOS Firebase Auth when needed
    // For now, return empty module
}
