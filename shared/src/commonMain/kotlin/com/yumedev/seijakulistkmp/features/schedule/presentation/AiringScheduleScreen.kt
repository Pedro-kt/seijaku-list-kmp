package com.yumedev.seijakulistkmp.features.schedule.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.DetailScreen
import com.yumedev.seijakulistkmp.features.schedule.domain.model.groupByTimeSlots
import com.yumedev.seijakulistkmp.features.schedule.presentation.components.CurrentTimeIndicator
import com.yumedev.seijakulistkmp.features.schedule.presentation.components.DayStickyHeader
import com.yumedev.seijakulistkmp.features.schedule.presentation.components.ScheduleCard
import com.yumedev.seijakulistkmp.features.schedule.presentation.components.ScheduleCardSkeleton
import com.yumedev.seijakulistkmp.features.schedule.presentation.components.TimeDivider
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowLeft
import dev.seyfarth.tablericons.outlined.CalendarEvent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

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

@Composable
fun getDayShortName(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(Res.string.day_monday_short)
        DayOfWeek.TUESDAY -> stringResource(Res.string.day_tuesday_short)
        DayOfWeek.WEDNESDAY -> stringResource(Res.string.day_wednesday_short)
        DayOfWeek.THURSDAY -> stringResource(Res.string.day_thursday_short)
        DayOfWeek.FRIDAY -> stringResource(Res.string.day_friday_short)
        DayOfWeek.SATURDAY -> stringResource(Res.string.day_saturday_short)
        DayOfWeek.SUNDAY -> stringResource(Res.string.day_sunday_short)
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
    val responsive = ResponsiveTheme.values
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val todayIndex = remember(state.scheduleDays) {
        state.scheduleDays.indexOfFirst { it.isToday }
    }

    val isScrolledAwayFromToday by remember {
        derivedStateOf {
            if (todayIndex == -1 || state.scheduleDays.isEmpty()) false
            else listState.firstVisibleItemIndex > todayIndex + 2 ||
                    listState.firstVisibleItemIndex < todayIndex - 2
        }
    }

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
        floatingActionButton = {
            AnimatedVisibility(
                visible = isScrolledAwayFromToday && todayIndex != -1 && !state.isLoading,
                enter = slideInVertically { it * 2 } + fadeIn(),
                exit = slideOutVertically { it * 2 } + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(todayIndex)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = TablerIcons.Outlined.CalendarEvent,
                        contentDescription = stringResource(Res.string.schedule_go_to_today),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.scheduleDays.isNotEmpty() && !state.isLoading) {
                ScrollableTabRow(
                    selectedTabIndex = state.scheduleDays.indexOfFirst { it.dayOfWeek == state.selectedDayOfWeek },
                    edgePadding = 0.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    divider = {}
                ) {
                    state.scheduleDays.forEach { day ->
                        Tab(
                            selected = day.dayOfWeek == state.selectedDayOfWeek,
                            onClick = {
                                viewModel.onDaySelected(day.dayOfWeek)
                                scope.launch {
                                    val dayIndex = state.scheduleDays.indexOf(day)
                                    if (dayIndex != -1) {
                                        var itemCount = 0
                                        val currentTime = Clock.System.now().toLocalDateTime(state.selectedTimezone.timeZone)
                                        val currentTimeMinutes = currentTime.hour * 60 + currentTime.minute

                                        for (i in 0 until dayIndex) {
                                            itemCount += 1
                                            if (state.scheduleDays[i].airingItems.isNotEmpty()) {
                                                val slots = state.scheduleDays[i].airingItems.groupByTimeSlots(
                                                    timeZone = state.selectedTimezone.timeZone,
                                                    slotIntervalMinutes = 30
                                                )

                                                var timeIndicatorAdded = false
                                                slots.forEach { slot ->
                                                    val slotTimeMinutes = slot.hour * 60 + slot.minute
                                                    if (state.scheduleDays[i].isToday && !timeIndicatorAdded && currentTimeMinutes < slotTimeMinutes) {
                                                        itemCount += 1
                                                        timeIndicatorAdded = true
                                                    }
                                                    itemCount += 1
                                                    itemCount += slot.items.size
                                                    itemCount += 1
                                                }
                                            } else {
                                                itemCount += 1
                                            }
                                        }
                                        listState.animateScrollToItem(itemCount)
                                    }
                                }
                            },
                            text = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = getDayShortName(day.dayOfWeek),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (day.dayOfWeek == state.selectedDayOfWeek) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Normal
                                        }
                                    )
                                    Text(
                                        text = day.date.dayOfMonth.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (day.isToday) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }

            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { viewModel.onRefresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    state.isLoading -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = responsive.mediumSpacing)
                        ) {
                            items(6) {
                                ScheduleCardSkeleton(
                                    modifier = Modifier.padding(
                                        horizontal = responsive.horizontalPadding,
                                        vertical = responsive.smallPadding
                                    )
                                )
                            }
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
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = responsive.mediumSpacing)
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
                                                .padding(responsive.xlargePadding),
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

                                    var currentTimeIndicatorInserted = false
                                    val currentTime = Clock.System.now()
                                        .toLocalDateTime(state.selectedTimezone.timeZone)
                                    val currentTimeMinutes =
                                        currentTime.hour * 60 + currentTime.minute

                                    timeSlots.forEach { timeSlot ->
                                        val slotTimeMinutes = timeSlot.hour * 60 + timeSlot.minute

                                        if (scheduleDay.isToday && !currentTimeIndicatorInserted && currentTimeMinutes < slotTimeMinutes) {
                                            item(key = "current_time_indicator_${scheduleDay.dayOfWeek}") {
                                                CurrentTimeIndicator(timezone = state.selectedTimezone.timeZone)
                                            }
                                            currentTimeIndicatorInserted = true
                                        }
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
                                                modifier = Modifier.padding(
                                                    horizontal = responsive.horizontalPadding,
                                                    vertical = responsive.smallPadding
                                                )
                                            )
                                        }

                                        item(key = "spacer_${scheduleDay.dayOfWeek}_${timeSlot.displayTime}") {
                                            Spacer(modifier = Modifier.height(responsive.mediumSpacing))
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
}
