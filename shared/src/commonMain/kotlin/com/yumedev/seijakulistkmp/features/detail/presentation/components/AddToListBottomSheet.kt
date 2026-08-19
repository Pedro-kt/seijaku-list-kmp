package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListPriority
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.CalendarEvent
import dev.seyfarth.tablericons.outlined.Minus
import dev.seyfarth.tablericons.outlined.Plus
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddToListBottomSheet(
    mediaTitle: String,
    mediaType: MediaType,
    mediaStatus: String?, // RELEASING, NOT_YET_RELEASED, FINISHED, etc.
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
    var selectedStatus by remember { mutableStateOf(currentStatus ?: getDefaultStatus(mediaType, mediaStatus)) }
    var progress by remember { mutableStateOf(currentProgress) }
    var score by remember { mutableStateOf(currentScore) }
    var note by remember { mutableStateOf(currentNote) }
    var startDate by remember { mutableStateOf(currentStartDate) }
    var rewatches by remember { mutableStateOf(currentRewatches) }
    var priority by remember { mutableStateOf(currentPriority) }

    val isAnime = mediaType == MediaType.ANIME
    val maxProgress = if (isAnime) totalEpisodes else totalChapters

    val isPlanToWatch = selectedStatus == MediaListStatus.PLANNING

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
                    getStatusOptions(mediaType).forEach { status ->
                        val isEnabled = isStatusEnabled(status, mediaStatus)
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = { if (isEnabled) selectedStatus = status },
                            label = {
                                Text(getStatusLabel(status, isAnime))
                            },
                            enabled = isEnabled
                        )
                    }
                }
            }

            if (isPlanToWatch) {
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
            } else {
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
                        text = buildString {
                            append(progress)
                            append(" / ")
                            append(maxProgress?.toString() ?: "?")
                            append(if (isAnime) " ep" else " cap")
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    FilledIconButton(
                        onClick = {
                            if (maxProgress == null || progress < maxProgress) {
                                progress++
                            }
                        },
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

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
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

                Slider(
                    value = score ?: 0f,
                    onValueChange = { value ->
                        val roundedValue = (value * 2).toInt() / 2f
                        score = if (roundedValue == 0f) null else roundedValue
                    },
                    valueRange = 0f..10f,
                    steps = 19,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.list_note),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { if (it.length <= 500) note = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    placeholder = {
                        Text(stringResource(Res.string.list_note_placeholder))
                    },
                    supportingText = {
                        Text(
                            text = "${note.length} / 500",
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 5
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.list_start_date),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedButton(
                        onClick = { /* TODO: Show date picker */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = TablerIcons.Outlined.CalendarEvent,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = startDate ?: "—",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.list_rewatches),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledIconButton(
                            onClick = { if (rewatches > 0) rewatches-- },
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Minus,
                                contentDescription = stringResource(Res.string.list_decrease),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = rewatches.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        FilledIconButton(
                            onClick = { rewatches++ },
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Plus,
                                contentDescription = stringResource(Res.string.list_increase),
                                modifier = Modifier.size(18.dp)
                            )
                        }
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
                            note,
                            startDate,
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

@Composable
private fun getPriorityLabel(priority: MediaListPriority): String {
    return when (priority) {
        MediaListPriority.HIGH -> stringResource(Res.string.list_priority_high)
        MediaListPriority.MEDIUM -> stringResource(Res.string.list_priority_medium)
        MediaListPriority.LOW -> stringResource(Res.string.list_priority_low)
    }
}

private fun getDefaultStatus(mediaType: MediaType, mediaStatus: String?): MediaListStatus {
    return when {
        mediaStatus == "RELEASING" || mediaStatus == "PUBLISHING" -> MediaListStatus.CURRENT
        mediaStatus == "FINISHED" -> MediaListStatus.COMPLETED
        else -> MediaListStatus.PLANNING
    }
}

private fun getStatusOptions(mediaType: MediaType): List<MediaListStatus> {
    return if (mediaType == MediaType.ANIME) {
        listOf(
            MediaListStatus.CURRENT,
            MediaListStatus.COMPLETED,
            MediaListStatus.PLANNING,
            MediaListStatus.PAUSED,
            MediaListStatus.DROPPED,
            MediaListStatus.REPEATING
        )
    } else {
        listOf(
            MediaListStatus.CURRENT,
            MediaListStatus.COMPLETED,
            MediaListStatus.PLANNING,
            MediaListStatus.PAUSED,
            MediaListStatus.DROPPED,
            MediaListStatus.REPEATING
        )
    }
}

private fun isStatusEnabled(status: MediaListStatus, mediaStatus: String?): Boolean {
    return when (mediaStatus) {
        "NOT_YET_RELEASED" -> status == MediaListStatus.PLANNING
        else -> true
    }
}
