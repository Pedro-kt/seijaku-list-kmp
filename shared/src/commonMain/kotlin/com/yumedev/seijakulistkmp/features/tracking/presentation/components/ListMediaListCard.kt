package com.yumedev.seijakulistkmp.features.tracking.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.DotsVertical
import dev.seyfarth.tablericons.outlined.Edit
import dev.seyfarth.tablericons.outlined.Plus
import dev.seyfarth.tablericons.outlined.Trash
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun ListMediaListCard(
    entry: MediaListEntry,
    onIncrementProgress: () -> Unit,
    onEditClick: () -> Unit,
    onStatusChange: (MediaListStatus) -> Unit,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = entry.mediaInfo?.coverImage,
                    contentDescription = entry.mediaInfo?.title,
                    modifier = Modifier
                        .width(85.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = entry.mediaInfo?.title ?: stringResource(Res.string.no_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            ListStatusChip(
                                status = entry.status,
                                mediaType = entry.mediaType
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val total = when (entry.mediaType) {
                                MediaType.ANIME -> entry.mediaInfo?.totalEpisodes
                                MediaType.MANGA -> entry.mediaInfo?.totalChapters
                            }

                            Text(
                                text = if (total != null) "${entry.progress}/$total" else "${entry.progress}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            entry.score?.let {
                                if (it > 0) {
                                    Text(
                                        text = "·",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = stringResource(Res.string.list_score_label, it),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (entry.repeatCount > 0) {
                                Text(
                                    text = "·",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = stringResource(Res.string.list_rewatches_count, entry.repeatCount),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = {
                                val total = when (entry.mediaType) {
                                    MediaType.ANIME -> entry.mediaInfo?.totalEpisodes
                                    MediaType.MANGA -> entry.mediaInfo?.totalChapters
                                }
                                if (total != null && total > 0) {
                                    (entry.progress.toFloat() / total.toFloat()).coerceIn(0f, 1f)
                                } else {
                                    0f
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box {
                            Surface(
                                onClick = { showMenu = true },
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = TablerIcons.Outlined.DotsVertical,
                                        contentDescription = stringResource(Res.string.filter_menu),
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(Res.string.list_edit_title)) },
                                    onClick = {
                                        showMenu = false
                                        onEditClick()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = TablerIcons.Outlined.Edit,
                                            contentDescription = null
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(Res.string.list_delete)) },
                                    onClick = {
                                        showMenu = false
                                        showDeleteDialog = true
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = TablerIcons.Outlined.Trash,
                                            contentDescription = null
                                        )
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.error,
                                        leadingIconColor = MaterialTheme.colorScheme.error
                                    )
                                )
                            }
                        }
                    }
                }
            }

            val canIncrement = entry.status == MediaListStatus.CURRENT ||
                    entry.status == MediaListStatus.REPEATING

            FilledIconButton(
                onClick = onIncrementProgress,
                enabled = canIncrement,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 8.dp)
                    .size(32.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(
                    imageVector = TablerIcons.Outlined.Plus,
                    contentDescription = stringResource(Res.string.list_increase),
                    modifier = Modifier.size(18.dp),
                    tint = if (canIncrement) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                    }
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = stringResource(Res.string.list_delete_confirm_title))
            },
            text = {
                Text(text = stringResource(Res.string.list_delete_confirm_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(Res.string.list_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(Res.string.list_cancel))
                }
            }
        )
    }
}

@Composable
private fun ListStatusChip(
    status: MediaListStatus,
    mediaType: MediaType,
    modifier: Modifier = Modifier
) {
    val statusText = when (status) {
        MediaListStatus.CURRENT -> if (mediaType == MediaType.ANIME) {
            stringResource(Res.string.list_status_watching_short)
        } else {
            stringResource(Res.string.list_status_reading_short)
        }
        MediaListStatus.COMPLETED -> stringResource(Res.string.list_status_completed)
        MediaListStatus.PLANNING -> if (mediaType == MediaType.ANIME) {
            stringResource(Res.string.list_status_plan_to_watch_short)
        } else {
            stringResource(Res.string.list_status_plan_to_read_short)
        }
        MediaListStatus.PAUSED -> stringResource(Res.string.list_status_paused)
        MediaListStatus.DROPPED -> stringResource(Res.string.list_status_dropped)
        MediaListStatus.REPEATING -> if (mediaType == MediaType.ANIME) {
            stringResource(Res.string.list_status_repeating)
        } else {
            stringResource(Res.string.list_status_rereading)
        }
    }

    val statusColor = when (status) {
        MediaListStatus.CURRENT -> MaterialTheme.colorScheme.primary
        MediaListStatus.COMPLETED -> Color(0xFF4CAF50)
        MediaListStatus.PLANNING -> MaterialTheme.colorScheme.secondary
        MediaListStatus.PAUSED -> Color(0xFFFFA726)
        MediaListStatus.DROPPED -> Color(0xFFEF5350)
        MediaListStatus.REPEATING -> MaterialTheme.colorScheme.tertiary
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = statusColor.copy(alpha = 0.2f),
        modifier = modifier
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall,
            color = statusColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
