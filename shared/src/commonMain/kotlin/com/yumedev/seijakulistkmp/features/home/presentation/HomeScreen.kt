package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.DetailScreen
import com.yumedev.seijakulistkmp.features.detail.presentation.components.AddToListBottomSheet
import com.yumedev.seijakulistkmp.features.detail.presentation.utils.toCore
import com.yumedev.seijakulistkmp.features.home.domain.model.SectionType
import com.yumedev.seijakulistkmp.features.home.presentation.components.AnimeSection
import com.yumedev.seijakulistkmp.features.home.presentation.components.FeaturedCarousel
import com.yumedev.seijakulistkmp.features.home.presentation.components.FeaturedCarouselSkeleton
import com.yumedev.seijakulistkmp.features.home.presentation.components.HomeLoadingSkeleton
import com.yumedev.seijakulistkmp.features.home.presentation.components.MangaSection
import com.yumedev.seijakulistkmp.features.search.presentation.components.QuickFilterChips
import com.yumedev.seijakulistkmp.features.search.presentation.model.QuickFilter
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem
import com.yumedev.seijakulistkmp.features.home.presentation.sectionlist.SectionListScreen
import com.yumedev.seijakulistkmp.features.search.presentation.SearchScreen
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.AlertCircle
import dev.seyfarth.tablericons.outlined.Calendar
import dev.seyfarth.tablericons.outlined.Book
import dev.seyfarth.tablericons.outlined.DeviceTv
import dev.seyfarth.tablericons.outlined.Search
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import seijakulistkmp.shared.generated.resources.*

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return

        val navCallback: (SectionType, MediaType, String) -> Unit = { sectionType, mediaType, title ->
            navigator.push(
                SectionListScreen(
                    sectionType = sectionType,
                    mediaType = mediaType,
                    title = title
                )
            )
        }

        HomeScreenContent(
            onNavigateToAnimeDetail = { animeId ->
                navigator.push(
                    DetailScreen(
                        animeId,
                        MediaType.ANIME
                    )
                )
            },
            onNavigateToMangaDetail = { mangaId ->
                navigator.push(
                    DetailScreen(
                        mangaId,
                        MediaType.MANGA
                    )
                )
            },
            onNavigateToSectionList = navCallback
        )
    }
}

@Composable
fun HomeScreenContent(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToAnimeDetail: (Int) -> Unit = {},
    onNavigateToMangaDetail: (Int) -> Unit = {},
    onNavigateToSectionList: (SectionType, MediaType, String) -> Unit
) {
    val animeViewModel = koinViewModel<AnimeHomeViewModel>()
    val animeState by animeViewModel.state.collectAsState()

    val mangaViewModel = koinViewModel<MangaHomeViewModel>()
    val mangaState by mangaViewModel.state.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 2 })
    val animeScrollState = rememberLazyListState()
    val mangaScrollState = rememberLazyListState()

    val isScrolled by remember {
        derivedStateOf {
            val currentScrollState = if (selectedTabIndex == 0) animeScrollState else mangaScrollState
            currentScrollState.firstVisibleItemIndex > 0 || currentScrollState.firstVisibleItemScrollOffset > 100
        }
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(Unit) {
        animeViewModel.loadAllSections()
    }

    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 1) {
            mangaViewModel.loadAllSections()
        }
    }

    Scaffold(
        topBar = {
            HomeTopAppBar(
                isScrolled = isScrolled,
                onSearchClick = onNavigateToSearch,
                onNotificationsClick = {
                    // TODO: Navigate to notifications screen
                },
                onProfileClick = {
                    // TODO: Navigate to profile
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        Box(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                )
                        )
                    }
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.DeviceTv,
                                contentDescription = null
                            )
                            Text(
                                text = stringResource(Res.string.nav_anime),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Book,
                                contentDescription = null
                            )
                            Text(
                                text = stringResource(Res.string.nav_manga),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                )
            }

            val showInitialLoading = if (selectedTabIndex == 0) {
                animeState.isInitialLoading
            } else {
                mangaState.isInitialLoading
            }

            if (showInitialLoading) {
                HomeLoadingSkeleton()
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = false
                ) { page ->
                    when (page) {
                        0 -> AnimeTabContent(
                                scrollState = animeScrollState,
                                state = animeState,
                                viewModel = animeViewModel,
                                onRefresh = { animeViewModel.refreshAll() },
                                onFeaturedRetry = { animeViewModel.retryAllAnime() },
                                onFeaturedInteraction = { animeViewModel.onFeaturedAnimeInteraction() },
                                onAiringNowRetry = { animeViewModel.retryAllAnime() },
                                onNextSeasonRetry = { animeViewModel.retryAllAnime() },
                                onTopRatedRetry = { animeViewModel.retryAllAnime() },
                                onAnimeClick = onNavigateToAnimeDetail,
                                onAnimeLongClick = { item -> animeViewModel.onCardLongPress(item.id) },
                                onNavigateToSectionList = onNavigateToSectionList
                            )
                        1 -> MangaTabContent(
                            scrollState = mangaScrollState,
                            state = mangaState,
                            viewModel = mangaViewModel,
                            onRefresh = { mangaViewModel.refreshAll() },
                            onFeaturedRetry = { mangaViewModel.retryAllManga() },
                            onFeaturedInteraction = { mangaViewModel.onFeaturedMangaInteraction() },
                            onPublishingRetry = { mangaViewModel.retryAllManga() },
                            onPopularRetry = { mangaViewModel.retryAllManga() },
                            onTopRatedRetry = { mangaViewModel.retryAllManga() },
                            onRecentlyAddedRetry = { mangaViewModel.retryAllManga() },
                            onManhwaRetry = { mangaViewModel.retryAllManga() },
                            onMangaClick = onNavigateToMangaDetail,
                            onMangaLongClick = { item -> mangaViewModel.onCardLongPress(item.id) },
                            onNavigateToSectionList = onNavigateToSectionList
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimeTabContent(
    scrollState: LazyListState,
    state: AnimeHomeState,
    viewModel: AnimeHomeViewModel,
    onRefresh: () -> Unit,
    onFeaturedRetry: () -> Unit,
    onFeaturedInteraction: () -> Unit,
    onAiringNowRetry: () -> Unit,
    onNextSeasonRetry: () -> Unit,
    onTopRatedRetry: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    onAnimeLongClick: (AnimeCardItem) -> Unit,
    onNavigateToSectionList: (SectionType, MediaType, String) -> Unit
) {
    val navigator = LocalNavigator.current

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        enabled = !state.isLoadingFeaturedAnime && state.featuredAnimeError == null,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
        item {
            when {
                state.isLoadingFeaturedAnime -> {
                    FeaturedCarouselSkeleton(modifier = Modifier.padding(top = 16.dp))
                }
                state.featuredAnimeError != null -> {
                    val title = state.featuredAnimeError.title
                    val hint = state.featuredAnimeError.hint

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(400))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = TablerIcons.Outlined.AlertCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(
                                            text = title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = hint,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            FilledTonalButton(
                                onClick = onFeaturedRetry,
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.retry),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
                state.featuredAnime.isNotEmpty() -> {
                    FeaturedCarousel(
                        items = state.featuredAnime,
                        currentPage = state.currentFeaturedAnimeIndex,
                        modifier = Modifier.padding(top = 16.dp),
                        onItemClick = { item -> onAnimeClick(item.id) },
                        onUserInteraction = onFeaturedInteraction
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            QuickFilterChips(
                onFilterClick = { filter, label ->
                    navigator?.push(
                        SearchScreen(quickFilter = filter)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            val airingNowTitle = stringResource(Res.string.airing_now)
        AnimeSection(
                title = airingNowTitle,
                items = state.airingNowAnime,
                onSeeMoreClick = {
                    onNavigateToSectionList(
                        SectionType.AIRING_NOW,
                        MediaType.ANIME,
                        airingNowTitle
                    )
                },
                onItemClick = { item -> onAnimeClick(item.id) },
                onItemLongClick = onAnimeLongClick
            )
        }

        item {
            val upcomingTitle = stringResource(Res.string.upcoming_releases)
            AnimeSection(
                title = upcomingTitle,
                items = state.nextSeasonAnime,
                onSeeMoreClick = {
                    onNavigateToSectionList(
                        SectionType.NEXT_SEASON,
                        MediaType.ANIME,
                        upcomingTitle
                    )
                },
                onItemClick = { item -> onAnimeClick(item.id) },
                onItemLongClick = onAnimeLongClick
            )
        }

        item {
            val topRatedTitle = stringResource(Res.string.top_rated)
            AnimeSection(
                title = topRatedTitle,
                items = state.topRatedAnime,
                onSeeMoreClick = {
                    onNavigateToSectionList(
                        SectionType.TOP_RATED_ANIME,
                        MediaType.ANIME,
                        topRatedTitle
                    )
                },
                onItemClick = { item -> onAnimeClick(item.id) },
                onItemLongClick = onAnimeLongClick
            )
        }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        if (state.isBottomSheetVisible && state.selectedMediaDetail != null) {
            val mediaDetail = state.selectedMediaDetail
            val listEntry = state.selectedMediaListEntry

            AddToListBottomSheet(
                mediaTitle = mediaDetail.title,
                mediaType = mediaDetail.type.toCore(),
                mediaStatus = mediaDetail.status,
                totalEpisodes = mediaDetail.episodes,
                totalChapters = null,
                currentProgress = listEntry?.progress ?: 0,
                currentScore = listEntry?.score,
                currentStatus = listEntry?.status,
                currentNote = listEntry?.notes ?: "",
                currentStartDate = listEntry?.startDate,
                currentRewatches = listEntry?.repeatCount ?: 0,
                currentPriority = listEntry?.priority ?: com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority.MEDIUM,
                onDismiss = { viewModel.dismissBottomSheet() },
                onSave = { status, progress, score, note, startDate, rewatches, priority ->
                    viewModel.saveToList(status, progress, score, note, startDate, rewatches, priority)
                }
            )
        }
    }
}

@Composable
private fun MangaTabContent(
    scrollState: LazyListState,
    state: MangaHomeState,
    viewModel: MangaHomeViewModel,
    onRefresh: () -> Unit,
    onFeaturedRetry: () -> Unit,
    onFeaturedInteraction: () -> Unit,
    onPublishingRetry: () -> Unit,
    onPopularRetry: () -> Unit,
    onTopRatedRetry: () -> Unit,
    onRecentlyAddedRetry: () -> Unit,
    onManhwaRetry: () -> Unit,
    onMangaClick: (Int) -> Unit,
    onMangaLongClick: (com.yumedev.seijakulistkmp.features.home.presentation.model.MangaCardItem) -> Unit,
    onNavigateToSectionList: (SectionType, MediaType, String) -> Unit
) {
    val navigator = LocalNavigator.current

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        enabled = !state.isLoadingFeaturedManga && state.featuredMangaError == null,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
        // Featured Manga Carousel
        item {
            when {
                state.isLoadingFeaturedManga -> {
                    FeaturedCarouselSkeleton(modifier = Modifier.padding(top = 16.dp))
                }
                state.featuredMangaError != null -> {
                    val title = state.featuredMangaError.title
                    val hint = state.featuredMangaError.hint

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(400))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = TablerIcons.Outlined.AlertCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(
                                            text = title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = hint,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            FilledTonalButton(
                                onClick = onFeaturedRetry,
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.retry),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
                state.featuredManga.isNotEmpty() -> {
                    FeaturedCarousel(
                        items = state.featuredManga,
                        currentPage = state.currentFeaturedMangaIndex,
                        modifier = Modifier.padding(top = 16.dp),
                        onItemClick = { item -> onMangaClick(item.id) },
                        onUserInteraction = onFeaturedInteraction
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            QuickFilterChips(
                onFilterClick = { filter, _ ->
                    navigator?.push(
                        SearchScreen(quickFilter = filter)
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Publishing Now Section
        item {
            val publishingNowTitle = stringResource(Res.string.publishing_now)
            MangaSection(
                title = publishingNowTitle,
                items = state.publishingManga,
                onSeeMoreClick = {
                    onNavigateToSectionList(
                        SectionType.PUBLISHING_MANGA,
                        MediaType.MANGA,
                        publishingNowTitle
                    )
                },
                onItemClick = { item -> onMangaClick(item.id) },
                onItemLongClick = onMangaLongClick
            )
        }

        // Popular Manga Section
        item {
            val popularMangaTitle = stringResource(Res.string.popular_manga)
            MangaSection(
                title = popularMangaTitle,
                items = state.popularManga,
                onSeeMoreClick = {
                    onNavigateToSectionList(
                        SectionType.POPULAR_MANGA,
                        MediaType.MANGA,
                        popularMangaTitle
                    )
                },
                onItemClick = { item -> onMangaClick(item.id) },
                onItemLongClick = onMangaLongClick
            )
        }

        // Top Rated Section
        item {
            val topRatedMangaTitle = stringResource(Res.string.top_rated_manga)
            MangaSection(
                title = topRatedMangaTitle,
                items = state.topRatedManga,
                onSeeMoreClick = {
                    onNavigateToSectionList(
                        SectionType.TOP_RATED_MANGA,
                        MediaType.MANGA,
                        topRatedMangaTitle
                    )
                },
                onItemClick = { item -> onMangaClick(item.id) },
                onItemLongClick = onMangaLongClick
            )
        }

        // Recently Added Section
        item {
            val recentlyAddedTitle = stringResource(Res.string.recently_added)
            MangaSection(
                title = recentlyAddedTitle,
                items = state.recentlyAddedManga,
                onSeeMoreClick = {
                    onNavigateToSectionList(
                        SectionType.RECENTLY_ADDED,
                        MediaType.MANGA,
                        recentlyAddedTitle
                    )
                },
                onItemClick = { item -> onMangaClick(item.id) },
                onItemLongClick = onMangaLongClick
            )
        }

        // Manhwa & Manhua Section
        item {
            val manhwaTitle = stringResource(Res.string.manhwa)
            MangaSection(
                title = manhwaTitle,
                items = state.manhwaManga,
                onSeeMoreClick = {
                    onNavigateToSectionList(
                        SectionType.MANHWA,
                        MediaType.MANGA,
                        manhwaTitle
                    )
                },
                onItemClick = { item -> onMangaClick(item.id) },
                onItemLongClick = onMangaLongClick
            )
        }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        if (state.isBottomSheetVisible && state.selectedMediaDetail != null) {
            val mediaDetail = state.selectedMediaDetail
            val listEntry = state.selectedMediaListEntry

            AddToListBottomSheet(
                mediaTitle = mediaDetail.title,
                mediaType = mediaDetail.type.toCore(),
                mediaStatus = mediaDetail.status,
                totalEpisodes = null,
                totalChapters = mediaDetail.chapters,
                currentProgress = listEntry?.progress ?: 0,
                currentScore = listEntry?.score,
                currentStatus = listEntry?.status,
                currentNote = listEntry?.notes ?: "",
                currentStartDate = listEntry?.startDate,
                currentRewatches = listEntry?.repeatCount ?: 0,
                currentPriority = listEntry?.priority ?: com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority.MEDIUM,
                onDismiss = { viewModel.dismissBottomSheet() },
                onSave = { status, progress, score, note, startDate, rewatches, priority ->
                    viewModel.saveToList(status, progress, score, note, startDate, rewatches, priority)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    isScrolled: Boolean,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.app_name)
            )
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                ExpandableSearchChip(
                    isExpanded = isScrolled,
                    onClick = onSearchClick
                )

                Surface(
                    onClick = if (isScrolled) onProfileClick else onNotificationsClick,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Calendar,
                            contentDescription = stringResource(Res.string.notifications),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun ExpandableSearchChip(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    // Animación del ancho con spring (muy expresivo)
    val chipWidth by animateDpAsState(
        targetValue = if (isExpanded) 120.dp else 40.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isExpanded) {
            MaterialTheme.colorScheme.surfaceContainerHighest
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        modifier = Modifier
            .width(chipWidth)
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = TablerIcons.Outlined.Search,
                contentDescription = stringResource(Res.string.search),
                tint = if (isExpanded) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.size(24.dp)
            )

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandHorizontally(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                Text(
                    text = stringResource(Res.string.search),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}
