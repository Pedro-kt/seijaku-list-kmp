package com.yumedev.seijakulistkmp.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.HttpNetworkTransport
import org.koin.dsl.module

val networkModule = module {
    single {
        ApolloClient.Builder()
            .networkTransport(
                HttpNetworkTransport.Builder()
                    .serverUrl("https://graphql.anilist.co")
                    .build()
            )
            .build()
    }
}
