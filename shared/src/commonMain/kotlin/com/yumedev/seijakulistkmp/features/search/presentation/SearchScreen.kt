package com.yumedev.seijakulistkmp.features.search.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class SearchScreen : Screen {
    @Composable
    override fun Content() {
        SearchScreenContent()
    }
}

@Composable
fun SearchScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Buscar",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
