package com.yumedev.seijakulistkmp.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.core.utils.rememberFilePicker
import com.yumedev.seijakulistkmp.core.utils.rememberImageManager
import com.yumedev.seijakulistkmp.features.profile.domain.model.UserProfile
import com.yumedev.seijakulistkmp.features.profile.presentation.model.ProfileError
import com.yumedev.seijakulistkmp.features.profile.presentation.model.ProfileUiState
import com.yumedev.seijakulistkmp.features.settings.presentation.SettingsScreen
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStats
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
    @OptIn(KoinExperimentalAPI::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinViewModel<ProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()

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
        )
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
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.my_profile)
                    )
                },
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Edit,
                            contentDescription = stringResource(Res.string.profile_edit_button),
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Settings,
                            contentDescription = stringResource(Res.string.settings),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                ),
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        when {
            uiState.isLoading && uiState.profile == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null && uiState.profile == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
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
                    selectedTab = uiState.selectedTab,
                    onTabChanged = onTabChanged,
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
                    onRemoveAvatar = onRemoveAvatar,
                    onRemoveBanner = onRemoveBanner,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )

                if (uiState.showEditDialog) {
                    EditProfileBottomSheet(
                        profile = uiState.profile,
                        onDismiss = onDismissEditDialog,
                        onSave = onSaveProfileInfo,
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
    selectedTab: Int,
    onTabChanged: (Int) -> Unit,
    onAvatarClick: () -> Unit,
    onBannerClick: () -> Unit,
    onRemoveAvatar: () -> Unit,
    onRemoveBanner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // Hero Section
        ProfileHeroSection(
            profile = profile,
            onAvatarClick = onAvatarClick,
            onBannerClick = onBannerClick,
            onRemoveAvatar = onRemoveAvatar,
            onRemoveBanner = onRemoveBanner,
            modifier = Modifier.fillMaxWidth(),
        )

        // Tabs
        PrimaryTabRow(selectedTabIndex = selectedTab) {
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

        // Stats Content
        val stats = if (selectedTab == 0) animeStats else mangaStats
        val isAnime = selectedTab == 0

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (stats != null) {
                ProfileStatsSection(
                    stats = stats,
                    isAnime = isAnime,
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileDistributionSection(stats = stats)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
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

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun ProfileHeroSection(
    profile: UserProfile,
    onAvatarClick: () -> Unit,
    onBannerClick: () -> Unit,
    onRemoveAvatar: () -> Unit,
    onRemoveBanner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAvatarMenu by remember { mutableStateOf(false) }
    var showBannerMenu by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
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
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        .clickable {
                            if (!profile.banner.isNullOrBlank()) {
                                showBannerMenu = true
                            } else {
                                onBannerClick()
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = TablerIcons.Outlined.Camera,
                        contentDescription = "Select banner",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )

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
            }

            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.BottomStart)
                    .size(80.dp)
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
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 76.dp, bottom = 2.dp)
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
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = profile.about,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                )
            }

            if (profile.isLocal) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.profile_local_badge),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileStatsSection(
    stats: MediaListStats,
    isAnime: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
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
        horizontalArrangement = Arrangement.spacedBy(12.dp),
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
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
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
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
    val percentage = if (total > 0) count.toFloat() / total else 0f

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
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
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
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
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var name by remember { mutableStateOf(profile.name) }
    var about by remember { mutableStateOf(profile.about ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
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

            Spacer(modifier = Modifier.height(4.dp))

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
                shape = RoundedCornerShape(12.dp),
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
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
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
                    shape = RoundedCornerShape(12.dp),
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
