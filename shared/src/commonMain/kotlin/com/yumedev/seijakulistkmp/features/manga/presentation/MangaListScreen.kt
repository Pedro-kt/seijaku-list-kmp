package com.yumedev.seijakulistkmp.features.manga.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.DetailScreen
import com.yumedev.seijakulistkmp.features.tracking.presentation.mangalist.UserMangaListScreenContent
import com.yumedev.seijakulistkmp.features.tracking.presentation.mangalist.UserMangaListViewModel
import org.koin.compose.viewmodel.koinViewModel

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
    val viewModel = koinViewModel<UserMangaListViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    UserMangaListScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToDetail = { mediaId ->
            navigator.push(DetailScreen(mediaId, MediaType.MANGA))
        }
    )
}
