package com.yumedev.seijakulistkmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.yumedev.seijakulistkmp.di.animeModule
import com.yumedev.seijakulistkmp.di.coreModule
import com.yumedev.seijakulistkmp.di.homeModule
import com.yumedev.seijakulistkmp.features.main.presentation.MainScreen
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(
            coreModule,
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
