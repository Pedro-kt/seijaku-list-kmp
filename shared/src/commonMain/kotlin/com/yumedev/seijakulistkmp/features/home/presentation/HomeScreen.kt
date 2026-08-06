package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.yumedev.seijakulistkmp.features.home.presentation.components.AnimeSection
import com.yumedev.seijakulistkmp.features.home.presentation.components.FeaturedCarousel
import com.yumedev.seijakulistkmp.features.home.presentation.components.MangaSection
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.AlertCircle
import dev.seyfarth.tablericons.outlined.Bell
import dev.seyfarth.tablericons.outlined.Book
import dev.seyfarth.tablericons.outlined.DeviceTv
import dev.seyfarth.tablericons.outlined.Search
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import seijakulistkmp.shared.generated.resources.*

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}

@Composable
fun HomeScreenContent(
    onNavigateToSearch: () -> Unit = {},
    onNavigateToAnimeDetail: (Int) -> Unit = {},
    onNavigateToMangaDetail: (Int) -> Unit = {}
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 2 })
    val animeScrollState = rememberLazyListState()
    val mangaScrollState = rememberLazyListState()

    // Detect scroll based on current tab's scroll state
    val isScrolled by remember {
        derivedStateOf {
            val currentScrollState = if (selectedTabIndex == 0) animeScrollState else mangaScrollState
            currentScrollState.firstVisibleItemIndex > 0 || currentScrollState.firstVisibleItemScrollOffset > 100
        }
    }

    // Sync pager with tab selection
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
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
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
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

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> AnimeTabContent(
                        scrollState = animeScrollState,
                        state = state,
                        onFeaturedRetry = { viewModel.retryLoadFeaturedAnime() },
                        onFeaturedInteraction = { viewModel.onFeaturedAnimeInteraction() },
                        onAiringNowRetry = { viewModel.retryLoadAiringNow() },
                        onNextSeasonRetry = { viewModel.retryLoadNextSeason() },
                        onTopRatedRetry = { viewModel.retryLoadTopRated() },
                        onAnimeClick = onNavigateToAnimeDetail
                    )
                    1 -> MangaTabContent(
                        scrollState = mangaScrollState,
                        state = state,
                        onFeaturedRetry = { viewModel.retryLoadFeaturedManga() },
                        onFeaturedInteraction = { viewModel.onFeaturedMangaInteraction() },
                        onPublishingRetry = { viewModel.retryLoadPublishingManga() },
                        onPopularRetry = { viewModel.retryLoadPopularManga() },
                        onTopRatedRetry = { viewModel.retryLoadTopRatedManga() },
                        onRecentlyAddedRetry = { viewModel.retryLoadRecentlyAddedManga() },
                        onManhwaRetry = { viewModel.retryLoadManhwaManga() },
                        onMangaClick = onNavigateToMangaDetail
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimeTabContent(
    scrollState: LazyListState,
    state: HomeState,
    onFeaturedRetry: () -> Unit,
    onFeaturedInteraction: () -> Unit,
    onAiringNowRetry: () -> Unit,
    onNextSeasonRetry: () -> Unit,
    onTopRatedRetry: () -> Unit,
    onAnimeClick: (Int) -> Unit
) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            when {
                state.isLoadingFeatured -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.featuredError != null -> {
                    val title = state.featuredError.title
                    val hint = state.featuredError.hint

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
            AnimeSection(
                title = stringResource(Res.string.airing_now),
                items = state.airingNowAnime,
                onSeeMoreClick = { /* TODO: Navigate to Airing Now list */ },
                onItemClick = { item -> onAnimeClick(item.id) }
            )
        }

        item {
            AnimeSection(
                title = stringResource(Res.string.seasonal),
                items = state.nextSeasonAnime,
                onSeeMoreClick = { /* TODO: Navigate to Next Season list */ },
                onItemClick = { item -> onAnimeClick(item.id) }
            )
        }

        item {
            AnimeSection(
                title = stringResource(Res.string.top_rated),
                items = state.topRatedAnime,
                onSeeMoreClick = { /* TODO: Navigate to Top Rated list */ },
                onItemClick = { item -> onAnimeClick(item.id) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun MangaTabContent(
    scrollState: LazyListState,
    state: HomeState,
    onFeaturedRetry: () -> Unit,
    onFeaturedInteraction: () -> Unit,
    onPublishingRetry: () -> Unit,
    onPopularRetry: () -> Unit,
    onTopRatedRetry: () -> Unit,
    onRecentlyAddedRetry: () -> Unit,
    onManhwaRetry: () -> Unit,
    onMangaClick: (Int) -> Unit
) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Featured Manga Carousel
        item {
            when {
                state.isLoadingFeatured -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.featuredError != null -> {
                    val title = state.featuredError.title
                    val hint = state.featuredError.hint

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

        // Publishing Now Section
        item {
            MangaSection(
                title = stringResource(Res.string.publishing_now),
                items = state.publishingManga,
                onSeeMoreClick = { /* TODO: Navigate to Publishing list */ },
                onItemClick = { item -> onMangaClick(item.id) }
            )
        }

        // Popular Manga Section
        item {
            MangaSection(
                title = stringResource(Res.string.popular_manga),
                items = state.popularManga,
                onSeeMoreClick = { /* TODO: Navigate to Popular list */ },
                onItemClick = { item -> onMangaClick(item.id) }
            )
        }

        // Top Rated Section
        item {
            MangaSection(
                title = stringResource(Res.string.top_rated_manga),
                items = state.topRatedManga,
                onSeeMoreClick = { /* TODO: Navigate to Top Rated list */ },
                onItemClick = { item -> onMangaClick(item.id) }
            )
        }

        // Recently Added Section
        item {
            MangaSection(
                title = stringResource(Res.string.recently_added),
                items = state.recentlyAddedManga,
                onSeeMoreClick = { /* TODO: Navigate to Recently Added list */ },
                onItemClick = { item -> onMangaClick(item.id) }
            )
        }

        // Manhwa & Manhua Section
        item {
            MangaSection(
                title = stringResource(Res.string.manhwa),
                items = state.manhwaManga,
                onSeeMoreClick = { /* TODO: Navigate to Manhwa list */ },
                onItemClick = { item -> onMangaClick(item.id) }
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = if (isScrolled) 3.dp else 0.dp
    ) {
        AnimatedContent(
            targetState = isScrolled,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "TopAppBarTransition"
        ) { scrolled ->
            if (scrolled) {
                SearchBarMode(
                    onSearchClick = onSearchClick,
                    onProfileClick = onProfileClick
                )
            } else {
                NormalAppBarMode(
                    onSearchClick = onSearchClick,
                    onNotificationsClick = onNotificationsClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NormalAppBarMode(
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.Search,
                    contentDescription = stringResource(Res.string.search)
                )
            }
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.Bell,
                    contentDescription = stringResource(Res.string.notifications)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarMode(
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        title = {
            Surface(
                onClick = onSearchClick,
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = TablerIcons.Outlined.Search,
                        contentDescription = stringResource(Res.string.search),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(Res.string.search_placeholder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onProfileClick) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Y",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
