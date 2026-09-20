package com.yumedev.seijakulistkmp.features.schedule.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.DetailScreen
import com.yumedev.seijakulistkmp.features.schedule.domain.model.groupByTimeSlots
import com.yumedev.seijakulistkmp.features.schedule.presentation.components.DayStickyHeader
import com.yumedev.seijakulistkmp.features.schedule.presentation.components.ScheduleCard
import com.yumedev.seijakulistkmp.features.schedule.presentation.components.TimeDivider
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowLeft
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.*

class AiringScheduleScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current ?: return
        val viewModel = koinViewModel<AiringScheduleViewModel>()

        AiringScheduleScreenContent(
            viewModel = viewModel,
            onNavigateBack = { navigator.pop() },
            onNavigateToAnimeDetail = { animeId ->
                navigator.push(DetailScreen(animeId, MediaType.ANIME))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiringScheduleScreenContent(
    viewModel: AiringScheduleViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onErrorDismissed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.schedule_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = TablerIcons.Outlined.ArrowLeft,
                            contentDescription = stringResource(Res.string.schedule_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.onRefresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.scheduleDays.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.schedule_no_schedule_available),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        state.scheduleDays.forEach { scheduleDay ->
                            stickyHeader(key = "header_${scheduleDay.dayOfWeek}") {
                                DayStickyHeader(
                                    scheduleDay = scheduleDay,
                                    timezone = state.selectedTimezone.timeZone
                                )
                            }

                            if (scheduleDay.airingItems.isEmpty()) {
                                item(key = "empty_${scheduleDay.dayOfWeek}") {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(Res.string.schedule_no_anime_today),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            } else {
                                val timeSlots = scheduleDay.airingItems.groupByTimeSlots(
                                    timeZone = state.selectedTimezone.timeZone,
                                    slotIntervalMinutes = 30
                                )

                                timeSlots.forEach { timeSlot ->
                                    item(key = "time_${scheduleDay.dayOfWeek}_${timeSlot.displayTime}") {
                                        TimeDivider(
                                            timeSlot = timeSlot,
                                            timezone = state.selectedTimezone.timeZone
                                        )
                                    }

                                    items(
                                        items = timeSlot.items,
                                        key = { "item_${it.id}" }
                                    ) { item ->
                                        ScheduleCard(
                                            item = item,
                                            timezone = state.selectedTimezone.timeZone,
                                            onClick = onNavigateToAnimeDetail,
                                            showTime = false,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                        )
                                    }

                                    item(key = "spacer_${scheduleDay.dayOfWeek}_${timeSlot.displayTime}") {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
