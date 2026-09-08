package com.yumedev.seijakulistkmp.features.anime.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.DetailScreen
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
    val navigator = LocalNavigator.currentOrThrow

    UserAnimeListScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToDetail = { mediaId ->
            navigator.push(DetailScreen(mediaId, MediaType.ANIME))
        }
    )
}
