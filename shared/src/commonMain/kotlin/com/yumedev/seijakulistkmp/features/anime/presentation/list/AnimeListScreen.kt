package com.yumedev.seijakulistkmp.features.anime.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class AnimeListScreen : Screen {
    @Composable
    override fun Content() {
        AnimeListScreenContent()
    }
}

@Composable
fun AnimeListScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Animes",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
