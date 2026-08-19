package com.yumedev.seijakulistkmp.features.anime.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.yumedev.seijakulistkmp.features.tracking.presentation.animelist.UserAnimeListScreenContent
import com.yumedev.seijakulistkmp.features.tracking.presentation.animelist.UserAnimeListViewModel
import org.koin.compose.viewmodel.koinViewModel

class AnimeListScreen : Screen {
    @Composable
    override fun Content() {
        AnimeListScreenContent()
    }
}

@Composable
fun AnimeListScreenContent() {
    val viewModel = koinViewModel<UserAnimeListViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    UserAnimeListScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
