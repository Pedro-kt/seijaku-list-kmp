package com.yumedev.seijakulistkmp.features.manga.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

/**
 * Manga list screen - Browse manga
 */
class MangaListScreen : Screen {
    @Composable
    override fun Content() {
        MangaListScreenContent()
    }
}

@Composable
fun MangaListScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Mangas",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
