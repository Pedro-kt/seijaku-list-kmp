package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.validator.MediaListValidator
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Star
import dev.seyfarth.tablericons.outlined.Check
import dev.seyfarth.tablericons.outlined.Clock
import dev.seyfarth.tablericons.outlined.Minus
import dev.seyfarth.tablericons.outlined.PlayerPause
import dev.seyfarth.tablericons.outlined.PlayerPlay
import dev.seyfarth.tablericons.outlined.Plus
import dev.seyfarth.tablericons.outlined.Repeat
import dev.seyfarth.tablericons.outlined.Star
import dev.seyfarth.tablericons.outlined.X
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

private fun shouldShowProgress(status: MediaListStatus): Boolean {
    return status == MediaListStatus.CURRENT || status == MediaListStatus.REPEATING
}

private fun shouldShowScore(status: MediaListStatus): Boolean {
    return status != MediaListStatus.PLANNING
}


private fun shouldShowRewatches(status: MediaListStatus): Boolean {
    return status == MediaListStatus.COMPLETED || status == MediaListStatus.REPEATING
}

private fun shouldShowPriority(status: MediaListStatus): Boolean {
    return status == MediaListStatus.PLANNING
}

private fun getDefaultStatus(mediaType: MediaType, mediaStatus: String?): MediaListStatus {
    return when {
        mediaStatus == "RELEASING" || mediaStatus == "PUBLISHING" -> MediaListStatus.CURRENT
        mediaStatus == "FINISHED" -> MediaListStatus.COMPLETED
        else -> MediaListStatus.PLANNING
    }
}

private fun getStatusOptions(mediaType: MediaType, mediaStatus: String?): List<MediaListStatus> {
    return MediaListValidator.getAvailableStatuses(mediaStatus, mediaType)
}

private fun isStatusEnabled(status: MediaListStatus, mediaStatus: String?): Boolean {
    return MediaListValidator.isStatusAllowed(status, mediaStatus)
}

@Composable
private fun getStatusLabel(status: MediaListStatus, isAnime: Boolean): String {
    return when (status) {
        MediaListStatus.CURRENT -> if (isAnime) {
            stringResource(Res.string.list_status_watching)
        } else {
            stringResource(Res.string.list_status_reading)
        }
        MediaListStatus.COMPLETED -> stringResource(Res.string.list_status_completed)
        MediaListStatus.PLANNING -> if (isAnime) {
            stringResource(Res.string.list_status_plan_to_watch)
        } else {
            stringResource(Res.string.list_status_plan_to_read)
        }
        MediaListStatus.PAUSED -> stringResource(Res.string.list_status_paused)
        MediaListStatus.DROPPED -> stringResource(Res.string.list_status_dropped)
        MediaListStatus.REPEATING -> if (isAnime) {
            stringResource(Res.string.list_status_repeating)
        } else {
            stringResource(Res.string.list_status_rereading)
        }
    }
}

private fun getStatusIcon(status: MediaListStatus): ImageVector {
    return when (status) {
        MediaListStatus.CURRENT -> TablerIcons.Outlined.PlayerPlay
        MediaListStatus.COMPLETED -> TablerIcons.Outlined.Check
        MediaListStatus.PLANNING -> TablerIcons.Outlined.Clock
        MediaListStatus.PAUSED -> TablerIcons.Outlined.PlayerPause
        MediaListStatus.DROPPED -> TablerIcons.Outlined.X
        MediaListStatus.REPEATING -> TablerIcons.Outlined.Repeat
    }
}

@Composable
private fun getPriorityLabel(priority: MediaListPriority): String {
    return when (priority) {
        MediaListPriority.HIGH -> stringResource(Res.string.list_priority_high)
        MediaListPriority.MEDIUM -> stringResource(Res.string.list_priority_medium)
        MediaListPriority.LOW -> stringResource(Res.string.list_priority_low)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddToListBottomSheet(
    mediaTitle: String,
    mediaType: MediaType,
    mediaStatus: String?,
    totalEpisodes: Int?,
    totalChapters: Int?,
    currentProgress: Int = 0,
    currentScore: Float? = null,
    currentStatus: MediaListStatus? = null,
    currentNote: String = "",
    currentStartDate: String? = null,
    currentRewatches: Int = 0,
    currentPriority: MediaListPriority = MediaListPriority.MEDIUM,
    onDismiss: () -> Unit,
    onSave: (
        status: MediaListStatus,
        progress: Int,
        score: Float?,
        note: String,
        startDate: String?,
        rewatches: Int,
        priority: MediaListPriority
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStatus by remember(currentStatus, mediaType, mediaStatus) {
        mutableStateOf(currentStatus ?: getDefaultStatus(mediaType, mediaStatus))
    }
    var progress by remember(currentProgress) { mutableStateOf(currentProgress) }
    var score by remember(currentScore) { mutableStateOf(currentScore) }
    var rewatches by remember(currentRewatches) { mutableStateOf(currentRewatches) }
    var priority by remember(currentPriority) { mutableStateOf(currentPriority) }

    var isInitialLoad by remember { mutableStateOf(true) }
    var previousStatus by remember { mutableStateOf(selectedStatus) }

    val isAnime = mediaType == MediaType.ANIME
    val totalProgress = if (isAnime) totalEpisodes else totalChapters
    val maxProgress = MediaListValidator.getMaxAllowedProgress(totalProgress, mediaStatus)

    val isPlanToWatch = selectedStatus == MediaListStatus.PLANNING

    LaunchedEffect(selectedStatus) {
        if (isInitialLoad) {
            isInitialLoad = false
            previousStatus = selectedStatus
            return@LaunchedEffect
        }

        val (newProgress, shouldIncrementRewatches) = MediaListValidator.calculateStatusChangeAdjustments(
            previousStatus = previousStatus,
            newStatus = selectedStatus,
            currentProgress = progress,
            totalProgress = totalProgress
        )

        progress = newProgress
        if (shouldIncrementRewatches) {
            rewatches++
        }

        previousStatus = selectedStatus
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.list_edit_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = mediaTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }

            // Status Section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.list_status),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    getStatusOptions(mediaType, mediaStatus).forEach { status ->
                        val isEnabled = isStatusEnabled(status, mediaStatus)
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = { if (isEnabled) selectedStatus = status },
                            label = {
                                Text(getStatusLabel(status, isAnime))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = getStatusIcon(status),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            enabled = isEnabled
                        )
                    }
                }
            }

            if (shouldShowPriority(selectedStatus)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.list_priority),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MediaListPriority.entries.forEach { priorityOption ->
                            FilterChip(
                                selected = priority == priorityOption,
                                onClick = { priority = priorityOption },
                                label = {
                                    Text(getPriorityLabel(priorityOption))
                                }
                            )
                        }
                    }
                }
            }

            if (shouldShowProgress(selectedStatus)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(
                            if (isAnime) Res.string.list_episodes_watched
                            else Res.string.list_chapters_read
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledIconButton(
                            onClick = { if (progress > 0) progress-- },
                            enabled = selectedStatus != MediaListStatus.COMPLETED && progress > 0,
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Minus,
                                contentDescription = stringResource(Res.string.list_decrease),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            text = buildString {
                                append(progress)
                                append(" / ")
                                append(maxProgress?.toString() ?: "-")
                                append(" ")
                                append(if (isAnime) "ep" else "cap")
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        FilledIconButton(
                            onClick = {
                                if (maxProgress == null || progress < maxProgress) {
                                    progress++
                                }
                            },
                            enabled = selectedStatus != MediaListStatus.COMPLETED && (maxProgress == null || progress < maxProgress),
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Plus,
                                contentDescription = stringResource(Res.string.list_increase),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    if (maxProgress != null && maxProgress > 0) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = { (progress.toFloat() / maxProgress.toFloat()).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                val percentage = ((progress.toFloat() / maxProgress.toFloat()) * 100).toInt()
                                Text(
                                    text = "$percentage%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            if (shouldShowScore(selectedStatus)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.list_your_score),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = score?.takeIf { it > 0f }?.let { scoreValue ->
                                val rounded = (scoreValue * 10).toInt() / 10.0
                                rounded.toString()
                            } ?: stringResource(Res.string.list_not_scored),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(10) { index ->
                            val starValue = index + 1
                            val isFilled = (score ?: 0f) >= starValue
                            Icon(
                                imageVector = if (isFilled) TablerIcons.Filled.Star else TablerIcons.Outlined.Star,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (isFilled) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                }
                            )
                            if (index < 9) {
                                Spacer(modifier = Modifier.width(2.dp))
                            }
                        }
                    }

                    Slider(
                        value = score ?: 0f,
                        onValueChange = { value ->
                            val roundedValue = (value * 2).toInt() / 2f
                            score = if (roundedValue == 0f) null else roundedValue
                        },
                        valueRange = 0f..10f,
                        steps = 19,
                        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                    )
                }
            }

            if (shouldShowRewatches(selectedStatus)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.list_rewatches),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledIconButton(
                            onClick = { if (rewatches > 0) rewatches-- },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Minus,
                                contentDescription = stringResource(Res.string.list_decrease)
                            )
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        Text(
                            text = rewatches.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(24.dp))

                        FilledIconButton(
                            onClick = { rewatches++ },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Plus,
                                contentDescription = stringResource(Res.string.list_increase)
                            )
                        }
                    }
                }
            }

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.list_cancel),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Button(
                        onClick = {
                            onSave(
                                selectedStatus,
                                progress,
                                score,
                                currentNote,
                                currentStartDate,
                                rewatches,
                                priority
                            )
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(Res.string.list_save),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                                                                                                          }
            }
        }
    }

