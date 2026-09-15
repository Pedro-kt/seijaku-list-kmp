package com.yumedev.seijakulistkmp.di

import android.content.Context
import com.yumedev.seijakulistkmp.features.auth.data.repository.AuthRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformAuthModule(googleWebClientId: String): Module = module {
    single<com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository> {
        AuthRepositoryImpl(
            context = androidContext(),
            webClientId = googleWebClientId
        )
    }
}
