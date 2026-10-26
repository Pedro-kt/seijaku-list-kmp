package com.yumedev.seijakulistkmp.features.character.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.core.error.ErrorType
import com.yumedev.seijakulistkmp.core.error.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.character.domain.model.CharacterDetail
import com.yumedev.seijakulistkmp.features.character.presentation.components.*
import com.yumedev.seijakulistkmp.features.character.presentation.mapper.toUiModel
import com.yumedev.seijakulistkmp.features.character.presentation.model.CharacterDetailStrings
import com.yumedev.seijakulistkmp.features.character.presentation.model.CharacterDetailUiModel
import com.yumedev.seijakulistkmp.features.detail.presentation.DetailScreen
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.AlertCircle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

data class CharacterScreen(
    val characterId: Int
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<CharacterViewModel>(key = "character_$characterId")
        val state by viewModel.state.collectAsState()

        LaunchedEffect(characterId) {
            viewModel.loadCharacterDetail(characterId)
        }

        when {
            state.error != null -> {
                ErrorContent(
                    error = state.error!!,
                    onRetry = { viewModel.retry(characterId) },
                    onBack = { navigator.pop() }
                )
            }
            state.characterDetail != null -> {
                val characterDetailUi = rememberCharacterDetailUiModel(state.characterDetail!!)

                CharacterScreenContent(
                    characterDetail = characterDetailUi,
                    onBackClick = { navigator.pop() },
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onMediaClick = { mediaId, mediaType ->
                        navigator.push(DetailScreen(mediaId, mediaType))
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
private fun rememberCharacterDetailUiModel(characterDetail: CharacterDetail): CharacterDetailUiModel {
    val strings = CharacterDetailStrings(
        mainRole = stringResource(Res.string.character_main_role),
        supportingRole = stringResource(Res.string.character_supporting_role),
        formatAnime = stringResource(Res.string.nav_anime),
        formatManga = stringResource(Res.string.nav_manga),
        formatMovie = stringResource(Res.string.format_movie),
        cmUnit = stringResource(Res.string.character_cm)
    )

    return remember(characterDetail, strings) {
        characterDetail.toUiModel(strings)
    }
}

@Composable
private fun CharacterScreenContent(
    characterDetail: CharacterDetailUiModel,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onMediaClick: (Int, com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    val scrollState = rememberScrollState()

    val showTitleInTopBar = scrollState.value > 350

    Scaffold(
        topBar = {
            CharacterTopBar(
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                isFavorite = characterDetail.isFavorite,
                title = if (showTitleInTopBar) characterDetail.name else null
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(responsive.xlargePadding)
        ) {
            CharacterHeader(
                imageUrl = characterDetail.imageUrl,
                name = characterDetail.name,
                nameNative = characterDetail.nameNative,
                roleLabel = characterDetail.roleLabel,
                formattedFavorites = characterDetail.formattedFavorites,
                formattedAppearances = characterDetail.formattedAppearances,
                formattedRanking = characterDetail.formattedRanking
            )

            CharacterInfo(
                age = characterDetail.age,
                height = characterDetail.height,
                birthday = characterDetail.birthday,
                nickname = characterDetail.nickname
            )

            CharacterBiography(
                description = characterDetail.description
            )

            CharacterAppearsIn(
                media = characterDetail.media,
                onMediaClick = onMediaClick
            )

            CharacterVoiceActors(
                voiceActors = characterDetail.voiceActors
            )

            Spacer(modifier = Modifier.height(responsive.largePadding))
        }
    }
}

@Composable
private fun LoadingContent(onBack: () -> Unit) {
    val responsive = ResponsiveTheme.values
    Scaffold(
        topBar = {
            CharacterTopBar(
                onBackClick = onBack,
                onFavoriteClick = {},
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
            verticalArrangement = Arrangement.spacedBy(responsive.xlargePadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = responsive.horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(responsive.largePadding)
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .size(responsive.largeIconSize * 3)
                        .clip(RoundedCornerShape(responsive.largeIconSize * 1.5f))
                )

                ShimmerBox(
                    modifier = Modifier
                        .width(responsive.cardWidth + responsive.largePadding)
                        .height(responsive.mediumIconSize)
                        .clip(RoundedCornerShape(responsive.mediumSpacing))
                )

                ShimmerBox(
                    modifier = Modifier
                        .width(responsive.cardWidth - responsive.largePadding)
                        .height(responsive.smallIconSize)
                        .clip(RoundedCornerShape(responsive.mediumSpacing))
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
                ) {
                    repeat(3) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(responsive.cornerRadius)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = responsive.largePadding),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
                            ) {
                                ShimmerBox(
                                    modifier = Modifier
                                        .size(responsive.minTouchTarget, responsive.smallIconSize)
                                        .clip(RoundedCornerShape(responsive.mediumSpacing))
                                )
                                ShimmerBox(
                                    modifier = Modifier
                                        .size(responsive.minTouchTarget + responsive.largePadding, responsive.smallIconSize)
                                        .clip(RoundedCornerShape(responsive.mediumSpacing))
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = responsive.horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
            ) {
                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(responsive.cardWidth - responsive.largePadding)
                                .height(responsive.smallIconSize)
                                .clip(RoundedCornerShape(responsive.mediumSpacing))
                        )
                        ShimmerBox(
                            modifier = Modifier
                                .width(responsive.cardWidth)
                                .height(responsive.smallIconSize)
                                .clip(RoundedCornerShape(responsive.mediumSpacing))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(responsive.largePadding))
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
    val responsive = ResponsiveTheme.values
    Scaffold(
        topBar = {
            CharacterTopBar(
                onBackClick = onBack,
                onFavoriteClick = {},
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
                verticalArrangement = Arrangement.spacedBy(responsive.largePadding),
                modifier = Modifier.padding(responsive.xlargePadding)
            ) {
                Icon(
                    imageVector = TablerIcons.Outlined.AlertCircle,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
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
