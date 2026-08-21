package com.yumedev.seijakulistkmp.features.detail.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.yumedev.seijakulistkmp.core.error.ErrorUiMapper
import com.yumedev.seijakulistkmp.core.utils.rememberShareHelper
import com.yumedev.seijakulistkmp.core.utils.rememberUrlOpener
import com.yumedev.seijakulistkmp.features.character.presentation.CharacterScreen
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.components.*
import com.yumedev.seijakulistkmp.features.detail.presentation.mapper.toUiModel
import com.yumedev.seijakulistkmp.features.detail.presentation.model.DetailStrings
import com.yumedev.seijakulistkmp.features.detail.presentation.model.MediaDetailUiModel
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
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
        val viewModel = koinViewModel<DetailViewModel>(key = "detail_${mediaId}_${mediaType.name}")
        val state by viewModel.state.collectAsState()
        val urlOpener = rememberUrlOpener()
        val shareHelper = rememberShareHelper()

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
                val mediaDetailUi = rememberMediaDetailUiModel(state.mediaDetail!!)

                DetailScreenContent(
                    mediaDetail = mediaDetailUi,
                    onBackClick = { navigator.pop() },
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onShareClick = {
                        val typeText = if (mediaDetailUi.type == MediaType.ANIME) "anime" else "manga"
                        val shareText = buildString {
                            append(mediaDetailUi.title)
                            append("\n\n")
                            append("https://anilist.co/$typeText/${mediaDetailUi.id}")
                        }
                        shareHelper.shareText(shareText, mediaDetailUi.title)
                    },
                    onAddToListClick = { viewModel.toggleFavorite() },
                    onSaveToList = { status, progress, score, note, startDate, rewatches, priority ->
                        viewModel.saveToList(status, progress, score, note, startDate, rewatches, priority)
                    },
                    onCharacterClick = { characterId ->
                        navigator.push(CharacterScreen(characterId))
                    },
                    onSeeAllChaptersClick = { /* TODO: Navigate to chapters list */ },
                    onChapterClick = { /* TODO: Navigate to chapter/episode */ },
                    onImageClick = { /* TODO: Open image viewer */ },
                    onExternalLinkClick = { url -> url?.let { urlOpener.openUrl(it) } },
                    onTrailerClick = { id, site ->
                        val url = when (site.lowercase()) {
                            "youtube" -> "https://www.youtube.com/watch?v=$id"
                            "dailymotion" -> "https://www.dailymotion.com/video/$id"
                            else -> "https://www.youtube.com/watch?v=$id"
                        }
                        urlOpener.openUrl(url)
                    }
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
                isFavorite = false,
                title = null
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(3) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 16.dp),
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
                isFavorite = false,
                title = null
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
                    text = stringResource(ErrorUiMapper.mapToStringResource(error)),
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

@Composable
private fun rememberMediaDetailUiModel(mediaDetail: MediaDetail): MediaDetailUiModel {
    val primaryColor = MaterialTheme.colorScheme.primary

    val strings = DetailStrings(
        statusFinished = stringResource(Res.string.status_finished),
        statusAiring = stringResource(Res.string.status_airing),
        statusNotYetAired = stringResource(Res.string.status_not_yet_aired),
        statusCancelled = stringResource(Res.string.status_cancelled),
        votes = stringResource(Res.string.detail_votes, ""),
        timeMinutes = stringResource(Res.string.detail_time_minutes),
        timeHours = stringResource(Res.string.detail_time_hours),
        timeDays = stringResource(Res.string.detail_time_days),
        characterRoleMain = stringResource(Res.string.character_role_main),
        characterRoleSupporting = stringResource(Res.string.character_role_supporting),
        infoType = stringResource(Res.string.detail_info_type),
        infoVolumes = stringResource(Res.string.detail_info_volumes),
        infoStatus = stringResource(Res.string.detail_info_status),
        infoChapters = stringResource(Res.string.detail_info_chapters),
        infoEpisodes = stringResource(Res.string.detail_info_episodes),
        infoPublication = stringResource(Res.string.detail_info_publication),
        infoSerialization = stringResource(Res.string.detail_info_serialization),
        infoAuthor = stringResource(Res.string.detail_info_author),
        infoArt = stringResource(Res.string.detail_info_art),
        infoDemographic = stringResource(Res.string.detail_info_demographic),
        infoLicense = stringResource(Res.string.detail_info_license),
        infoStudio = stringResource(Res.string.detail_info_studio)
    )

    return remember(mediaDetail, strings, primaryColor) {
        mediaDetail.toUiModel(strings, primaryColor)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    mediaDetail: MediaDetailUiModel,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onAddToListClick: () -> Unit,
    onSaveToList: (MediaListStatus, Int, Float?, String, String?, Int, MediaListPriority) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onSeeAllChaptersClick: () -> Unit,
    onChapterClick: (Int) -> Unit,
    onImageClick: (String) -> Unit,
    onExternalLinkClick: (String?) -> Unit,
    onTrailerClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddToListBottomSheet by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val showTitleInTopBar = scrollState.value > 300

    Scaffold(
        topBar = {
            DetailTopBar(
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                onShareClick = onShareClick,
                isFavorite = mediaDetail.isFavorite,
                title = if (showTitleInTopBar) mediaDetail.title else null
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            DetailHeader(
                coverImageUrl = mediaDetail.coverImageUrl,
                typeLabel = mediaDetail.typeLabel,
                demographic = mediaDetail.demographic,
                title = mediaDetail.title,
                titleNative = mediaDetail.titleNative,
                metadataText = mediaDetail.headerMetadata,
                formattedScore = mediaDetail.formattedScore,
                formattedVotes = mediaDetail.formattedVotes,
                rankingText = mediaDetail.rankingText,
                popularityText = mediaDetail.popularityText,
                nextAiringText = mediaDetail.nextAiringText,
                onAddToListClick = { showAddToListBottomSheet = true }
            )

            DetailSynopsis(
                synopsis = mediaDetail.description
            )

            DetailInformation(
                items = mediaDetail.informationItems
            )

            mediaDetail.trailer?.let { trailer ->
                DetailTrailer(
                    trailer = trailer,
                    onTrailerClick = onTrailerClick
                )
            }

            DetailGenres(
                genres = mediaDetail.genres
            )

            DetailCharacters(
                characters = mediaDetail.characters,
                onCharacterClick = onCharacterClick
            )

            if (mediaDetail.type == MediaType.ANIME) {
                DetailExternalLinks(
                    links = mediaDetail.externalLinks,
                    onLinkClick = onExternalLinkClick
                )
            }

            DetailChaptersList(
                type = mediaDetail.type,
                chapters = mediaDetail.chaptersList,
                episodes = mediaDetail.episodesList,
                totalCount = mediaDetail.totalChapters ?: mediaDetail.totalEpisodes,
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

    if (showAddToListBottomSheet) {
        AddToListBottomSheet(
            mediaTitle = mediaDetail.title,
            mediaType = when (mediaDetail.type) {
                MediaType.ANIME -> com.yumedev.seijakulistkmp.core.domain.model.MediaType.ANIME
                MediaType.MANGA -> com.yumedev.seijakulistkmp.core.domain.model.MediaType.MANGA
            },
            mediaStatus = mediaDetail.status,
            totalEpisodes = mediaDetail.totalEpisodes,
            totalChapters = mediaDetail.totalChapters,
            onDismiss = { showAddToListBottomSheet = false },
            onSave = { status, progress, score, note, startDate, rewatches, priority ->
                onSaveToList(status, progress, score, note, startDate, rewatches, priority)
            }
        )
    }
}
