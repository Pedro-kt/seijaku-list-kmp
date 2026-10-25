package com.yumedev.seijakulistkmp.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.core.utils.rememberFilePicker
import com.yumedev.seijakulistkmp.core.utils.rememberImageManager
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode
import com.yumedev.seijakulistkmp.features.profile.presentation.components.EditFavoriteEpisodeBottomSheet
import com.yumedev.seijakulistkmp.features.profile.presentation.components.FavoriteEpisodesSection
import com.yumedev.seijakulistkmp.features.profile.presentation.components.FavoriteMediaSelectorBottomSheet
import com.yumedev.seijakulistkmp.features.profile.presentation.components.ProfileTopFavoritesSection
import com.yumedev.seijakulistkmp.features.profile.presentation.model.ProfileError
import com.yumedev.seijakulistkmp.features.profile.presentation.model.ProfileUiState
import com.yumedev.seijakulistkmp.features.settings.presentation.SettingsScreen
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetMediaListUseCase
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Camera
import dev.seyfarth.tablericons.outlined.Edit
import dev.seyfarth.tablericons.outlined.Photo
import dev.seyfarth.tablericons.outlined.Settings
import dev.seyfarth.tablericons.outlined.Trash
import dev.seyfarth.tablericons.outlined.User
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.list_status_completed
import seijakulistkmp.shared.generated.resources.list_status_dropped
import seijakulistkmp.shared.generated.resources.list_status_paused
import seijakulistkmp.shared.generated.resources.list_status_plan_to_watch
import seijakulistkmp.shared.generated.resources.list_status_watching
import seijakulistkmp.shared.generated.resources.my_profile
import seijakulistkmp.shared.generated.resources.profile_about_label
import seijakulistkmp.shared.generated.resources.profile_add_banner
import seijakulistkmp.shared.generated.resources.profile_anilist_sync_unavailable
import seijakulistkmp.shared.generated.resources.profile_cancel_button
import seijakulistkmp.shared.generated.resources.profile_distribution
import seijakulistkmp.shared.generated.resources.profile_edit_button
import seijakulistkmp.shared.generated.resources.profile_edit_subtitle
import seijakulistkmp.shared.generated.resources.profile_edit_title
import seijakulistkmp.shared.generated.resources.profile_error_loading
import seijakulistkmp.shared.generated.resources.profile_error_saving
import seijakulistkmp.shared.generated.resources.profile_error_updating_stats
import seijakulistkmp.shared.generated.resources.profile_local_badge
import seijakulistkmp.shared.generated.resources.profile_member_since
import seijakulistkmp.shared.generated.resources.profile_name_label
import seijakulistkmp.shared.generated.resources.profile_no_data
import seijakulistkmp.shared.generated.resources.profile_save_button
import seijakulistkmp.shared.generated.resources.profile_stats_chapters
import seijakulistkmp.shared.generated.resources.profile_stats_episodes
import seijakulistkmp.shared.generated.resources.profile_stats_score
import seijakulistkmp.shared.generated.resources.profile_stats_time
import seijakulistkmp.shared.generated.resources.profile_stats_total
import seijakulistkmp.shared.generated.resources.profile_stats_volumes
import seijakulistkmp.shared.generated.resources.profile_tab_anime
import seijakulistkmp.shared.generated.resources.profile_tab_manga
import seijakulistkmp.shared.generated.resources.profile_select_image
import seijakulistkmp.shared.generated.resources.profile_remove_image
import seijakulistkmp.shared.generated.resources.settings
import kotlin.math.roundToInt
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
private fun ProfileError.toStringResource(): String {
    return when (this) {
        ProfileError.LoadingError -> stringResource(Res.string.profile_error_loading)
        ProfileError.SavingError -> stringResource(Res.string.profile_error_saving)
        ProfileError.UpdatingStatsError -> stringResource(Res.string.profile_error_updating_stats)
        ProfileError.AnilistSyncUnavailable -> stringResource(Res.string.profile_anilist_sync_unavailable)
    }
}

class ProfileScreen : Screen {
    @OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<ProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        var selectedEpisode by remember { mutableStateOf<FavoriteEpisode?>(null) }
        val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        LaunchedEffect(uiState.favoriteEpisodes) {
            selectedEpisode = selectedEpisode?.let { current ->
                uiState.favoriteEpisodes.find { it.id == current.id }
            }
        }

        ProfileScreenContent(
            uiState = uiState,
            onSettingsClick = { navigator.push(SettingsScreen()) },
            onEditProfile = viewModel::onEditProfile,
            onDismissEditDialog = viewModel::onDismissEditDialog,
            onSaveProfileInfo = viewModel::onSaveProfileInfo,
            onUpdateAvatar = viewModel::onUpdateAvatar,
            onUpdateBanner = viewModel::onUpdateBanner,
            onRemoveAvatar = viewModel::onRemoveAvatar,
            onRemoveBanner = viewModel::onRemoveBanner,
            onRefreshStatistics = viewModel::onRefreshStatistics,
            onTabChanged = viewModel::onTabChanged,
            onOpenFavoriteSelector = viewModel::onOpenFavoriteSelector,
            onDismissFavoriteSelector = viewModel::onDismissFavoriteSelector,
            onSetFavorite = viewModel::onSetFavorite,
            onRemoveFavorite = viewModel::onRemoveFavorite,
            onReorderFavorites = viewModel::onReorderFavorites,
            onEpisodeFavoriteClick = { episode ->
                selectedEpisode = episode
            },
            onToggleEpisodesSearch = viewModel::onToggleEpisodesSearch,
            onEpisodesSearchQueryChange = viewModel::onEpisodesSearchQueryChange,
            onEpisodesSortChange = viewModel::onEpisodesSortChange,
            onToggleAnimeGroup = viewModel::onToggleAnimeGroup
        )


        selectedEpisode?.let { episode ->
            val getMediaList: GetMediaListUseCase = koinInject()
            val allAnimeEntries by getMediaList(MediaType.ANIME).collectAsState(initial = emptyList())
            val animeTitle = allAnimeEntries.find { it.mediaId == episode.mediaId }?.mediaInfo?.title

            EditFavoriteEpisodeBottomSheet(
                episode = episode,
                animeTitle = animeTitle,
                sheetState = editSheetState,
                onDismiss = { selectedEpisode = null },
                onSave = { rating, comment ->
                    viewModel.onUpdateFavoriteEpisode(episode.id, rating, comment)
                    selectedEpisode = null
                },
                onDelete = {
                    viewModel.onDeleteFavoriteEpisode(episode.id)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState,
    onSettingsClick: () -> Unit,
    onEditProfile: () -> Unit,
    onDismissEditDialog: () -> Unit,
    onSaveProfileInfo: (String, String?) -> Unit,
    onUpdateAvatar: (String) -> Unit,
    onUpdateBanner: (String) -> Unit,
    onRemoveAvatar: () -> Unit,
    onRemoveBanner: () -> Unit,
    onRefreshStatistics: () -> Unit,
    onTabChanged: (Int) -> Unit,
    onOpenFavoriteSelector: (Int) -> Unit,
    onDismissFavoriteSelector: () -> Unit,
    onSetFavorite: (Int, Int) -> Unit,
    onRemoveFavorite: (Int) -> Unit,
    onReorderFavorites: (Int, Int, Boolean) -> Unit,
    onEpisodeFavoriteClick: (FavoriteEpisode) -> Unit,
    onToggleEpisodesSearch: () -> Unit,
    onEpisodesSearchQueryChange: (String) -> Unit,
    onEpisodesSortChange: (com.yumedev.seijakulistkmp.features.profile.presentation.model.EpisodeSortOption) -> Unit,
    onToggleAnimeGroup: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading && uiState.profile == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null && uiState.profile == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(uiState.error.toStringResource())
                }
            }

            uiState.profile != null -> {
                val filePicker = rememberFilePicker()
                val imageManager = rememberImageManager()

                ProfileContent(
                    profile = uiState.profile,
                    animeStats = uiState.animeStats,
                    mangaStats = uiState.mangaStats,
                    favoriteAnime = uiState.favoriteAnime,
                    favoriteManga = uiState.favoriteManga,
                    favoriteEpisodes = uiState.favoriteEpisodes,
                    groupedFavoriteEpisodes = uiState.groupedFavoriteEpisodes,
                    isFavoriteEpisodesLoading = uiState.isFavoriteEpisodesLoading,
                    isEpisodesSearchVisible = uiState.isEpisodesSearchVisible,
                    episodesSearchQuery = uiState.episodesSearchQuery,
                    episodesSortBy = uiState.episodesSortBy,
                    expandedAnimeGroups = uiState.expandedAnimeGroups,
                    selectedTab = uiState.selectedTab,
                    isAuthenticated = uiState.isAuthenticated,
                    onTabChanged = onTabChanged,
                    onEditProfile = onEditProfile,
                    onSettingsClick = onSettingsClick,
                    onAddFavorite = onOpenFavoriteSelector,
                    onFavoriteClick = { entry -> },
                    onReorderFavorites = onReorderFavorites,
                    onEpisodeFavoriteClick = onEpisodeFavoriteClick,
                    onToggleEpisodesSearch = onToggleEpisodesSearch,
                    onEpisodesSearchQueryChange = onEpisodesSearchQueryChange,
                    onEpisodesSortChange = onEpisodesSortChange,
                    onToggleAnimeGroup = onToggleAnimeGroup,
                    onAvatarClick = {
                        filePicker.pickImage(
                            onImageSelected = { uri ->
                                imageManager.saveImage(
                                    imageUri = uri,
                                    destinationFileName = "avatar_${System.currentTimeMillis()}.jpg",
                                    onSuccess = { path ->
                                        onUpdateAvatar(path)
                                    },
                                    onError = { /* TODO: Handle error */ }
                                )
                            },
                            onError = { /* TODO: Handle error */ }
                        )
                    },
                    onRemoveAvatar = onRemoveAvatar,
                    modifier = Modifier.fillMaxSize(),
                )

                if (uiState.showEditDialog) {
                    EditProfileBottomSheet(
                        profile = uiState.profile,
                        onDismiss = onDismissEditDialog,
                        onSave = onSaveProfileInfo,
                        onBannerClick = {
                            filePicker.pickImage(
                                onImageSelected = { uri ->
                                    imageManager.saveImage(
                                        imageUri = uri,
                                        destinationFileName = "banner_${System.currentTimeMillis()}.jpg",
                                        onSuccess = { path ->
                                            onUpdateBanner(path)
                                        },
                                        onError = { /* TODO: Handle error */ }
                                    )
                                },
                                onError = { /* TODO: Handle error */ }
                            )
                        },
                        onRemoveBanner = onRemoveBanner,
                    )
                }

                if (uiState.showFavoriteSelectorDialog && uiState.selectedFavoritePosition != null) {
                    val getMediaList: GetMediaListUseCase = koinInject()
                    val mediaType = if (uiState.selectedTab == 0) MediaType.ANIME else MediaType.MANGA
                    val allEntries by getMediaList(mediaType).collectAsState(initial = emptyList())

                    val excludedMediaIds = if (uiState.selectedTab == 0) {
                        uiState.favoriteAnime.map { it.mediaId }.toSet()
                    } else {
                        uiState.favoriteManga.map { it.mediaId }.toSet()
                    }

                    FavoriteMediaSelectorBottomSheet(
                        allEntries = allEntries,
                        excludedMediaIds = excludedMediaIds,
                        position = uiState.selectedFavoritePosition,
                        onDismiss = onDismissFavoriteSelector,
                        onSelect = { mediaId ->
                            onSetFavorite(mediaId, uiState.selectedFavoritePosition)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    profile: UserProfile,
    animeStats: MediaListStats?,
    mangaStats: MediaListStats?,
    favoriteAnime: List<com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry>,
    favoriteManga: List<com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry>,
    favoriteEpisodes: List<com.yumedev.seijakulistkmp.features.detail.domain.model.FavoriteEpisode>,
    groupedFavoriteEpisodes: List<com.yumedev.seijakulistkmp.features.profile.presentation.model.AnimeGroup>,
    isFavoriteEpisodesLoading: Boolean,
    isEpisodesSearchVisible: Boolean,
    episodesSearchQuery: String,
    episodesSortBy: com.yumedev.seijakulistkmp.features.profile.presentation.model.EpisodeSortOption,
    expandedAnimeGroups: Set<Int>,
    selectedTab: Int,
    isAuthenticated: Boolean,
    onTabChanged: (Int) -> Unit,
    onEditProfile: () -> Unit,
    onSettingsClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onRemoveAvatar: () -> Unit,
    onAddFavorite: (Int) -> Unit,
    onFavoriteClick: (com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry) -> Unit,
    onReorderFavorites: (Int, Int, Boolean) -> Unit,
    onEpisodeFavoriteClick: (FavoriteEpisode) -> Unit,
    onToggleEpisodesSearch: () -> Unit,
    onEpisodesSearchQueryChange: (String) -> Unit,
    onEpisodesSortChange: (com.yumedev.seijakulistkmp.features.profile.presentation.model.EpisodeSortOption) -> Unit,
    onToggleAnimeGroup: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val responsive = ResponsiveTheme.values

    val scrollOffset = scrollState.value
    val maxScroll = 200f
    val scrollAlpha = (scrollOffset / maxScroll).coerceIn(0f, 1f)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp),
        ) {
            ProfileBannerBackground(
                banner = profile.banner,
                alpha = 1f - scrollAlpha,
                modifier = Modifier.fillMaxWidth()
            )
        ProfileHeroSection(
            profile = profile,
            isAuthenticated = isAuthenticated,
            onAvatarClick = onAvatarClick,
            onRemoveAvatar = onRemoveAvatar,
            modifier = Modifier.fillMaxWidth(),
        )

        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier
                .padding(vertical = responsive.mediumPadding)
                .graphicsLayer {
                    translationY = -responsive.minTouchTarget.toPx()
                }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { onTabChanged(0) },
                text = { Text(stringResource(Res.string.profile_tab_anime)) },
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { onTabChanged(1) },
                text = { Text(stringResource(Res.string.profile_tab_manga)) },
            )
        }

        val stats = if (selectedTab == 0) animeStats else mangaStats
        val isAnime = selectedTab == 0

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = -responsive.minTouchTarget.toPx()
                },
            verticalArrangement = Arrangement.spacedBy(responsive.largePadding),
        ) {
            if (stats != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = responsive.horizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(responsive.largePadding)
                ) {
                    ProfileStatsSection(
                        stats = stats,
                        isAnime = isAnime,
                    )

                    Spacer(modifier = Modifier.height(responsive.mediumSpacing))

                    ProfileDistributionSection(stats = stats)
                }

                Spacer(modifier = Modifier.height(responsive.largePadding))

                val favorites = if (isAnime) favoriteAnime else favoriteManga
                ProfileTopFavoritesSection(
                    favorites = favorites,
                    isAnime = isAnime,
                    onAddFavorite = onAddFavorite,
                    onFavoriteClick = onFavoriteClick,
                    onReorderFavorites = onReorderFavorites,
                    modifier = Modifier.fillMaxWidth()
                )


                if (isAnime) {
                    Spacer(modifier = Modifier.height(responsive.xlargePadding))

                    val getMediaList: GetMediaListUseCase = koinInject()
                    val allAnimeEntries by getMediaList(MediaType.ANIME).collectAsState(initial = emptyList())
                    val animeTitles = allAnimeEntries.associate { entry ->
                        entry.mediaId to (entry.mediaInfo?.title ?: "")
                    }

                    FavoriteEpisodesSection(
                        groupedEpisodes = groupedFavoriteEpisodes,
                        animeTitles = animeTitles,
                        isLoading = isFavoriteEpisodesLoading,
                        isSearchVisible = isEpisodesSearchVisible,
                        searchQuery = episodesSearchQuery,
                        currentSort = episodesSortBy,
                        expandedGroups = expandedAnimeGroups,
                        onEpisodeClick = onEpisodeFavoriteClick,
                        onToggleSearch = onToggleEpisodesSearch,
                        onSearchQueryChange = onEpisodesSearchQueryChange,
                        onSortChange = onEpisodesSortChange,
                        onToggleGroup = onToggleAnimeGroup,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(responsive.xlargePadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.profile_no_data),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    )
                }
            }
        }

        }

        ProfileTopAppBar(
            onEditClick = onEditProfile,
            onSettingsClick = onSettingsClick,
            backgroundAlpha = scrollAlpha,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ProfileBannerBackground(
    banner: String?,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    val bannerHeight = if (!banner.isNullOrBlank()) 180.dp else 80.dp

    Box(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Spacer(modifier = Modifier.height(56.dp))
            Spacer(modifier = Modifier.height(bannerHeight))
        }

        if (!banner.isNullOrBlank()) {
            AsyncImage(
                model = banner,
                contentDescription = "Profile banner",
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { this.alpha = alpha },
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { this.alpha = alpha }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.surface
                            ),
                        ),
                    ),
            )
        }

        if (!banner.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopAppBar(
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    backgroundAlpha: Float,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = backgroundAlpha))
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = responsive.horizontalPadding),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
        Surface(
            onClick = onEditClick,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = TablerIcons.Outlined.Edit,
                    contentDescription = stringResource(Res.string.profile_edit_button),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.width(responsive.smallSpacing))

        Surface(
            onClick = onSettingsClick,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = TablerIcons.Outlined.Settings,
                    contentDescription = stringResource(Res.string.settings),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        }
    }
}

@Composable
fun ProfileHeroSection(
    profile: UserProfile,
    isAuthenticated: Boolean,
    onAvatarClick: () -> Unit,
    onRemoveAvatar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val responsive = ResponsiveTheme.values
    var showAvatarMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = -50.dp.toPx()
                }
        ) {
            Box(
                modifier = Modifier
                    .padding(start = responsive.horizontalPadding)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center,
            ) {
                if (profile.avatar?.large != null) {
                    AsyncImage(
                        model = profile.avatar.large,
                        contentDescription = "Profile avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = TablerIcons.Outlined.User,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(start = 86.dp, top = 70.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        if (profile.avatar?.large != null) {
                            showAvatarMenu = true
                        } else {
                            onAvatarClick()
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = TablerIcons.Outlined.Camera,
                    contentDescription = "Select avatar",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )

                DropdownMenu(
                    expanded = showAvatarMenu,
                    onDismissRequest = { showAvatarMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.profile_select_image)) },
                        onClick = {
                            showAvatarMenu = false
                            onAvatarClick()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = TablerIcons.Outlined.Photo,
                                contentDescription = null
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.profile_remove_image)) },
                        onClick = {
                            showAvatarMenu = false
                            onRemoveAvatar()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = TablerIcons.Outlined.Trash,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = responsive.horizontalPadding)
                .padding(top = responsive.smallSpacing)
                .graphicsLayer {
                    translationY = -40.dp.toPx()
                },
            verticalArrangement = Arrangement.spacedBy(responsive.smallPadding),
        ) {
            Text(
                text = profile.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "@${profile.name.lowercase().replace(" ", "")}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )

            val localDate = remember(profile.createdAt) {
                profile.createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
            }
            val monthNames = listOf(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            )

            Text(
                text = stringResource(
                    Res.string.profile_member_since,
                    monthNames[localDate.monthNumber - 1],
                    localDate.year
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )

            if (!profile.about.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(responsive.smallSpacing))
                Text(
                    text = profile.about,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                )
            }

            if (!isAuthenticated) {
                Spacer(modifier = Modifier.height(responsive.smallSpacing))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    shape = RoundedCornerShape(responsive.smallSpacing),
                ) {
                    Text(
                        text = stringResource(Res.string.profile_local_badge),
                        modifier = Modifier.padding(horizontal = responsive.mediumPadding, vertical = responsive.smallPadding),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

        }
    }
}

@Composable
fun ProfileStatsSection(
    stats: MediaListStats,
    isAnime: Boolean,
    modifier: Modifier = Modifier,
) {
    val responsive = ResponsiveTheme.values

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding),
    ) {
        StatCard(
            value = stats.totalEntries.toString(),
            label = stringResource(Res.string.profile_stats_total),
            modifier = Modifier.weight(1f),
        )
        StatCard(
            value = stats.averageScore?.let { (it * 10).roundToInt() / 10.0 }?.toString() ?: "—",
            label = stringResource(Res.string.profile_stats_score),
            modifier = Modifier.weight(1f),
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding),
    ) {
        if (isAnime) {
            StatCard(
                value = stats.totalProgress.toString(),
                label = stringResource(Res.string.profile_stats_episodes),
                modifier = Modifier.weight(1f),
            )
            StatCard(
                value = "—",
                label = stringResource(Res.string.profile_stats_time),
                modifier = Modifier.weight(1f),
            )
        } else {
            StatCard(
                value = stats.totalProgress.toString(),
                label = stringResource(Res.string.profile_stats_chapters),
                modifier = Modifier.weight(1f),
            )
            StatCard(
                value = "—",
                label = stringResource(Res.string.profile_stats_volumes),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    val responsive = ResponsiveTheme.values

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
        shape = RoundedCornerShape(responsive.cornerRadius),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(responsive.largePadding),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(responsive.smallPadding))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
fun ProfileDistributionSection(
    stats: MediaListStats,
    modifier: Modifier = Modifier,
) {
    val responsive = ResponsiveTheme.values

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding),
    ) {
        Text(
            text = stringResource(Res.string.profile_distribution),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        val distribution = listOf(
            stringResource(Res.string.list_status_completed) to stats.completedCount,
            stringResource(Res.string.list_status_watching) to stats.currentCount,
            stringResource(Res.string.list_status_plan_to_watch) to stats.planningCount,
            stringResource(Res.string.list_status_paused) to stats.pausedCount,
            stringResource(Res.string.list_status_dropped) to stats.droppedCount,
        )

        distribution.forEach { (status, count) ->
            DistributionItem(
                status = status,
                count = count,
                total = stats.totalEntries,
            )
        }
    }
}

@Composable
fun DistributionItem(
    status: String,
    count: Int,
    total: Int,
    modifier: Modifier = Modifier,
) {
    val responsive = ResponsiveTheme.values
    val percentage = if (total > 0) count.toFloat() / total else 0f

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(responsive.smallPadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(responsive.smallPadding)
                .clip(RoundedCornerShape(responsive.smallPadding / 2)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBottomSheet(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (String, String?) -> Unit,
    onBannerClick: () -> Unit,
    onRemoveBanner: () -> Unit,
) {
    val responsive = ResponsiveTheme.values
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var name by remember { mutableStateOf(profile.name) }
    var about by remember { mutableStateOf(profile.about ?: "") }
    var showBannerMenu by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = responsive.xlargePadding)
                .padding(bottom = responsive.xlargePadding),
            verticalArrangement = Arrangement.spacedBy(responsive.largePadding),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(responsive.smallPadding),
            ) {
                Text(
                    text = stringResource(Res.string.profile_edit_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(Res.string.profile_edit_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(responsive.smallPadding))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(responsive.cornerRadius))
                    .clickable {
                        if (profile.banner != null) {
                            showBannerMenu = true
                        } else {
                            onBannerClick()
                        }
                    }
            ) {
                if (!profile.banner.isNullOrBlank()) {
                    AsyncImage(
                        model = profile.banner,
                        contentDescription = "Profile banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    ),
                                ),
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Photo,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(Res.string.profile_add_banner),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = showBannerMenu,
                    onDismissRequest = { showBannerMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.profile_select_image)) },
                        onClick = {
                            showBannerMenu = false
                            onBannerClick()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = TablerIcons.Outlined.Photo,
                                contentDescription = null
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.profile_remove_image)) },
                        onClick = {
                            showBannerMenu = false
                            onRemoveBanner()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = TablerIcons.Outlined.Trash,
                                contentDescription = null
                            )
                        }
                    )
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(Res.string.profile_name_label)) },
                leadingIcon = {
                    Icon(
                        imageVector = TablerIcons.Outlined.User,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(responsive.cornerRadius),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                ),
            )

            OutlinedTextField(
                value = about,
                onValueChange = { about = it },
                label = { Text(stringResource(Res.string.profile_about_label)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(responsive.cornerRadius),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                ),
            )

            Spacer(modifier = Modifier.height(responsive.smallSpacing))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(responsive.cornerRadius),
                ) {
                    Text(
                        text = stringResource(Res.string.profile_cancel_button),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                Button(
                    onClick = {
                        onSave(
                            name,
                            about.takeIf { it.isNotBlank() },
                        )
                    },
                    modifier = Modifier.weight(1f),
                    enabled = name.isNotBlank(),
                    shape = RoundedCornerShape(responsive.cornerRadius),
                ) {
                    Text(
                        text = stringResource(Res.string.profile_save_button),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
