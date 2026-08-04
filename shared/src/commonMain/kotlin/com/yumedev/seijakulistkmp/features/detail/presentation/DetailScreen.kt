package com.yumedev.seijakulistkmp.features.detail.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.features.detail.domain.model.Character
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.components.*
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.AlertCircle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import seijakulistkmp.shared.generated.resources.*

data class DetailScreen(
    val mediaId: Int,
    val mediaType: MediaType
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<DetailViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(mediaId, mediaType) {
            viewModel.loadMediaDetail(mediaId, mediaType)
        }

        when {
            state.error != null -> {
                ErrorContent(
                    error = state.error!!,
                    onRetry = { viewModel.retry(mediaId, mediaType) },
                    onBack = { navigator.pop() }
                )
            }
            state.mediaDetail != null -> {
                DetailScreenContent(
                    mediaDetail = state.mediaDetail!!,
                    onBackClick = { navigator.pop() },
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onShareClick = { /* TODO: Implement share */ },
                    onMoreClick = { /* TODO: Implement more options */ },
                    onAddToListClick = { viewModel.addToList() },
                    onCharacterClick = { /* TODO: Navigate to character detail */ },
                    onSeeAllChaptersClick = { /* TODO: Navigate to chapters list */ },
                    onChapterClick = { /* TODO: Navigate to chapter/episode */ },
                    onImageClick = { /* TODO: Open image viewer */ }
                )
            }
            else -> {
                LoadingContent(onBack = { navigator.pop() })
            }
        }
    }
}

@Composable
private fun LoadingContent(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            DetailTopBar(
                onBackClick = onBack,
                onFavoriteClick = {},
                onShareClick = {},
                onMoreClick = {},
                isFavorite = false
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(140.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(120.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        ShimmerBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        ShimmerBox(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(20.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(3) { index ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ShimmerBox(
                                    modifier = Modifier
                                        .size(48.dp, 24.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                ShimmerBox(
                                    modifier = Modifier
                                        .size(60.dp, 16.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }

                            if (index < 2) {
                                VerticalDivider(
                                    modifier = Modifier.fillMaxHeight(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }
                }

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                repeat(4) {
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ShimmerBox(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset(translateAnim.value - 1000f, 0f),
        end = androidx.compose.ui.geometry.Offset(translateAnim.value, 0f)
    )

    Box(
        modifier = modifier.background(brush)
    )
}

@Composable
private fun ErrorContent(
    error: ErrorType,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            DetailTopBar(
                onBackClick = onBack,
                onFavoriteClick = {},
                onShareClick = {},
                onMoreClick = {},
                isFavorite = false
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    imageVector = TablerIcons.Outlined.AlertCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = when (error) {
                        ErrorType.NetworkError -> stringResource(Res.string.error_network)
                        ErrorType.ServerError -> stringResource(Res.string.error_server)
                        ErrorType.ServerUnavailable -> stringResource(Res.string.error_server_unavailable)
                        ErrorType.UnknownError -> stringResource(Res.string.error_unknown)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(onClick = onRetry) {
                    Text(stringResource(Res.string.retry))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    mediaDetail: MediaDetail,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onMoreClick: () -> Unit,
    onAddToListClick: () -> Unit,
    onCharacterClick: (Character) -> Unit,
    onSeeAllChaptersClick: () -> Unit,
    onChapterClick: (Int) -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DetailTopBar(
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                onShareClick = onShareClick,
                onMoreClick = onMoreClick,
                isFavorite = mediaDetail.isFavorite
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DetailHeader(
                coverImageUrl = mediaDetail.coverImageUrl,
                type = mediaDetail.format,
                demographic = mediaDetail.demographic,
                title = mediaDetail.title,
                titleNative = mediaDetail.titleNative,
                year = mediaDetail.year,
                format = mediaDetail.format,
                chapters = mediaDetail.chapters,
                volumes = mediaDetail.volumes,
                episodes = mediaDetail.episodes,
                status = mediaDetail.status,
                averageScore = mediaDetail.averageScore,
                totalVotes = mediaDetail.totalVotes,
                rankingPosition = mediaDetail.rankingPosition,
                popularityPosition = mediaDetail.popularityPosition,
                onAddToListClick = onAddToListClick
            )

            DetailSynopsis(
                synopsis = mediaDetail.description
            )

            DetailInformation(
                type = mediaDetail.type,
                format = mediaDetail.format,
                volumes = mediaDetail.volumes,
                status = mediaDetail.status,
                chapters = mediaDetail.chapters,
                episodes = mediaDetail.episodes,
                startDate = mediaDetail.startDate,
                endDate = mediaDetail.endDate,
                serialization = mediaDetail.serialization,
                author = mediaDetail.author,
                artist = mediaDetail.artist,
                studio = mediaDetail.studio,
                demographic = mediaDetail.demographic,
                license = mediaDetail.license
            )

            mediaDetail.trailer?.let { trailer ->
                DetailTrailer(
                    trailer = trailer,
                    onTrailerClick = { id, site ->
                        // TODO: Open trailer in browser or YouTube app
                    }
                )
            }

            DetailGenres(
                genres = mediaDetail.genres
            )

            DetailCharacters(
                characters = mediaDetail.mainCharacters,
                onCharacterClick = onCharacterClick
            )

            DetailChaptersList(
                type = mediaDetail.type,
                chapters = mediaDetail.chapters_list,
                episodes = mediaDetail.episodes_list,
                totalCount = mediaDetail.chapters ?: mediaDetail.episodes,
                onSeeAllClick = onSeeAllChaptersClick,
                onItemClick = onChapterClick
            )

            DetailImagesGrid(
                images = mediaDetail.images,
                onImageClick = onImageClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
