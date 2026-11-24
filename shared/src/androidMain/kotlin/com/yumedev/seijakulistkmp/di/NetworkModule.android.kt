package com.yumedev.seijakulistkmp.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.DefaultHttpEngine
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

actual val platformNetworkModule = module {
    single {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        ApolloClient.Builder()
            .serverUrl("https://graphql.anilist.co")
            .httpEngine(DefaultHttpEngine(okHttpClient))
            .build()
    }
}
