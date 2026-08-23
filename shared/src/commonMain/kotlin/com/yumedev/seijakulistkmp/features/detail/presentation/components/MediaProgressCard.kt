package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.CalendarEvent
import dev.seyfarth.tablericons.outlined.Edit
import dev.seyfarth.tablericons.filled.Star
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun MediaProgressCard(
    entry: MediaListEntry,
    mediaType: MediaType,
    totalEpisodes: Int?,
    totalChapters: Int?,
    onIncrementProgress: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(
                    status = entry.status,
                    mediaType = mediaType
                )

                entry.score?.let { score ->
                    if (score > 0) {
                        ScoreIndicator(score = score)
                    }
                }
            }

            ProgressSection(
                progress = entry.progress,
                total = when (mediaType) {
                    MediaType.ANIME -> totalEpisodes
                    MediaType.MANGA -> totalChapters
                },
                mediaType = mediaType
            )

            ActionButtons(
                mediaType = mediaType,
                progress = entry.progress,
                total = when (mediaType) {
                    MediaType.ANIME -> totalEpisodes
                    MediaType.MANGA -> totalChapters
                },
                onEditClick = onEditClick,
                onIncrementProgress = onIncrementProgress
            )

            entry.startDate?.let { startDate ->
                DateInfo(
                    startDate = startDate,
                    updatedAt = entry.updatedAt
                )
            }
        }
    }
}

@Composable
private fun StatusChip(
    status: MediaListStatus,
    mediaType: MediaType,
    modifier: Modifier = Modifier
) {
    val statusText = when (status) {
        MediaListStatus.CURRENT -> if (mediaType == MediaType.ANIME) {
            stringResource(Res.string.list_status_watching)
        } else {
            stringResource(Res.string.list_status_reading)
        }
        MediaListStatus.COMPLETED -> stringResource(Res.string.list_status_completed)
        MediaListStatus.PLANNING -> if (mediaType == MediaType.ANIME) {
            stringResource(Res.string.list_status_plan_to_watch)
        } else {
            stringResource(Res.string.list_status_plan_to_read)
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
        modifier = modifier,
        shape = CircleShape,
        color = statusColor.copy(alpha = 0.12f)
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelLarge,
            color = statusColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun ScoreIndicator(
    score: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${stringResource(Res.string.list_your_score)}:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = String.format("%.1f", score),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ProgressSection(
    progress: Int,
    total: Int?,
    mediaType: MediaType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (total != null) "$progress / $total ${
                    if (mediaType == MediaType.ANIME) {
                        stringResource(Res.string.episodes_suffix)
                    } else {
                        stringResource(Res.string.chapters_short)
                    }
                }" else "$progress ${
                    if (mediaType == MediaType.ANIME) {
                        stringResource(Res.string.episodes_suffix)
                    } else {
                        stringResource(Res.string.chapters_short)
                    }
                }",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (total != null && total > 0) {
                val percentage = ((progress.toFloat() / total.toFloat()) * 100).toInt()
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (total != null && total > 0) {
            LinearProgressIndicator(
                progress = { (progress.toFloat() / total.toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun ActionButtons(
    mediaType: MediaType,
    progress: Int,
    total: Int?,
    onEditClick: () -> Unit,
    onIncrementProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canIncrement = total == null || progress < total

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onEditClick,
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = TablerIcons.Outlined.Edit,
                contentDescription = stringResource(Res.string.list_edit_title),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.list_edit_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Button(
            onClick = onIncrementProgress,
            enabled = canIncrement,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "+1 ${
                    if (mediaType == MediaType.ANIME) {
                        stringResource(Res.string.episodes_suffix)
                    } else {
                        stringResource(Res.string.chapters_short)
                    }
                }",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DateInfo(
    startDate: String,
    updatedAt: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = TablerIcons.Outlined.CalendarEvent,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column {
                Text(
                    text = stringResource(Res.string.detail_started),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = startDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
