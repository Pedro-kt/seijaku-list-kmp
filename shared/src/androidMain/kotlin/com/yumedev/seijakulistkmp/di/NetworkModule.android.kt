package com.yumedev.seijakulistkmp.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.DefaultHttpEngine
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

actual val platformNetworkModule = module {
    single {
        val certificatePinner = CertificatePinner.Builder()
            .add("graphql.anilist.co", "sha256/uMDUd9ojdxyNhnUWneraEc0PZii9oJBVNnQrmxt6dJY=")
            .add("graphql.anilist.co", "sha256/GUAL5bejH7czkXcAeJ0vCiRxwMnVBsDlBMBsFtfLF8A=")
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
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
