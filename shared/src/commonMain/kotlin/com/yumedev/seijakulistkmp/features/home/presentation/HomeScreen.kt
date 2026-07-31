package com.yumedev.seijakulistkmp.features.home.presentation

import androidx.compose.animation.AnimatedContent
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
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Bell
import dev.seyfarth.tablericons.outlined.Book
import dev.seyfarth.tablericons.outlined.DeviceTv
import dev.seyfarth.tablericons.outlined.Search
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}

@Composable
fun HomeScreenContent() {
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
                onSearchClick = {
                    // TODO: Navigate to search or open search dialog
                },
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

            // Horizontal Pager (scroll disabled)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> AnimeTabContent(scrollState = animeScrollState)
                    1 -> MangaTabContent(scrollState = mangaScrollState)
                }
            }
        }
    }
}

/**
 * Anime tab content
 */
@Composable
private fun AnimeTabContent(scrollState: LazyListState) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(30) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Anime ${index + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Description for anime item",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

/**
 * Manga tab content
 */
@Composable
private fun MangaTabContent(scrollState: LazyListState) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(30) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Manga ${index + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Description for manga item",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
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
                // Search bar mode when scrolled
                SearchBarMode(
                    onSearchClick = onSearchClick,
                    onProfileClick = onProfileClick
                )
            } else {
                // Normal app bar mode
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
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
