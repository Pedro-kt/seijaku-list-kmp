package com.yumedev.seijakulistkmp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.yumedev.seijakulistkmp.di.animeModule
import com.yumedev.seijakulistkmp.di.characterModule
import com.yumedev.seijakulistkmp.di.coreModule
import com.yumedev.seijakulistkmp.di.detailModule
import com.yumedev.seijakulistkmp.di.homeModule
import com.yumedev.seijakulistkmp.di.networkModule
import com.yumedev.seijakulistkmp.di.searchModule
import com.yumedev.seijakulistkmp.di.settingsModule
import com.yumedev.seijakulistkmp.di.trackingModule
import com.yumedev.seijakulistkmp.features.main.presentation.MainScreen
import com.yumedev.seijakulistkmp.features.settings.domain.model.ThemeMode
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetThemeModeUseCase
import com.yumedev.seijakulistkmp.ui.theme.SeijakuTheme
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

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
            homeModule,
            searchModule,
            detailModule,
            characterModule,
            settingsModule,
            trackingModule
        )
    }) {
        val getThemeModeUseCase: GetThemeModeUseCase = koinInject()
        val themeMode by getThemeModeUseCase().collectAsState(initial = ThemeMode.SYSTEM)
        val isSystemInDark = isSystemInDarkTheme()

        val darkTheme = when (themeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDark
        }

        SeijakuTheme(darkTheme = darkTheme) {
            Navigator(MainScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}
