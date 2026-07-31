package com.yumedev.seijakulistkmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.yumedev.seijakulistkmp.di.animeModule
import com.yumedev.seijakulistkmp.di.coreModule
import com.yumedev.seijakulistkmp.di.homeModule
import com.yumedev.seijakulistkmp.di.networkModule
import com.yumedev.seijakulistkmp.features.main.presentation.MainScreen
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .crossfade(true)
            .build()
    }

    KoinApplication(application = {
        modules(
            coreModule,
            networkModule,
            animeModule,
            homeModule
        )
    }) {
        MaterialTheme {
            Navigator(MainScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}
