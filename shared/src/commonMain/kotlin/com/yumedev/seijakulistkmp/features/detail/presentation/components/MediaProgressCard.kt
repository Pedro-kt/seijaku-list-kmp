package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.domain.validator.MediaListValidator
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
    mediaStatus: String?,
    totalEpisodes: Int?,
    totalChapters: Int?,
    onIncrementProgress: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 4f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Card(
        onClick = { },
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation.dp
        ),
        interactionSource = interactionSource
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    entry.score?.let { score ->
                        if (score > 0) {
                            CompactScoreIndicator(score = score)
                        }
                    }

                    val progressText = when (mediaType) {
                        MediaType.ANIME -> totalEpisodes?.let { "${entry.progress}/$it" } ?: "${entry.progress}"
                        MediaType.MANGA -> totalChapters?.let { "${entry.progress}/$it" } ?: "${entry.progress}"
                    }
                    Text(
                        text = progressText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            ActionButtons(
                mediaType = mediaType,
                mediaStatus = mediaStatus,
                status = entry.status,
                progress = entry.progress,
                total = when (mediaType) {
                    MediaType.ANIME -> totalEpisodes
                    MediaType.MANGA -> totalChapters
                },
                onEditClick = onEditClick,
                onIncrementProgress = onIncrementProgress
            )
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

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.2f else 0.12f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        onClick = { },
        modifier = modifier.scale(scale),
        shape = CircleShape,
        color = statusColor.copy(alpha = backgroundAlpha),
        interactionSource = interactionSource
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
private fun CompactScoreIndicator(
    score: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = TablerIcons.Filled.Star,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color(0xFFFFA726)
        )
        Text(
            text = String.format("%.1f", score),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ActionButtons(
    mediaType: MediaType,
    mediaStatus: String?,
    status: MediaListStatus,
    progress: Int,
    total: Int?,
    onEditClick: () -> Unit,
    onIncrementProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canIncrement = MediaListValidator.canIncrementProgress(
        currentStatus = status,
        currentProgress = progress,
        total = total,
        mediaStatus = mediaStatus
    )

    val editInteractionSource = remember { MutableInteractionSource() }
    val isEditPressed by editInteractionSource.collectIsPressedAsState()

    val editScale by animateFloatAsState(
        targetValue = if (isEditPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val editIconScale by animateFloatAsState(
        targetValue = if (isEditPressed) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )

    val incrementInteractionSource = remember { MutableInteractionSource() }
    val isIncrementPressed by incrementInteractionSource.collectIsPressedAsState()

    val incrementScale by animateFloatAsState(
        targetValue = if (isIncrementPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val incrementElevation by animateFloatAsState(
        targetValue = if (isIncrementPressed) 6f else 2f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(
            onClick = onEditClick,
            modifier = Modifier.scale(editScale)
        ) {
            Icon(
                imageVector = TablerIcons.Outlined.Edit,
                contentDescription = stringResource(Res.string.list_edit_title),
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Button(
            onClick = onIncrementProgress,
            enabled = canIncrement,
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .scale(incrementScale),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = incrementElevation.dp,
                pressedElevation = 0.dp
            ),
            interactionSource = incrementInteractionSource
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
