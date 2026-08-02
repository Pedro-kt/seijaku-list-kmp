package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaFormat
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaStatus
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.toLabel
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchFilterBottomSheet(
    mediaType: SearchFilter,
    selectedStatus: MediaStatus,
    selectedFormat: MediaFormat,
    minScore: Int?,
    onStatusChange: (MediaStatus) -> Unit,
    onFormatChange: (MediaFormat) -> Unit,
    onMinScoreChange: (Int?) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAnime = mediaType == SearchFilter.ANIME
    val availableFormats = if (isAnime) {
        MediaFormat.forAnime()
    } else {
        MediaFormat.forManga()
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(Res.string.filters_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.filter_status),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MediaStatus.all().forEach { status ->
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = { onStatusChange(status) },
                            label = { Text(status.toLabel(isAnime)) }
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
                        text = stringResource(Res.string.filter_min_score),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (minScore != null) {
                            (minScore / 10.0).toString()
                        } else {
                            stringResource(Res.string.filter_score_any)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Slider(
                    value = (minScore ?: 0) / 10f,
                    onValueChange = { value ->
                        val scoreValue = (value * 10).toInt()
                        onMinScoreChange(if (scoreValue == 0) null else scoreValue)
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
                    text = stringResource(Res.string.filter_format),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableFormats.forEach { format ->
                        FilterChip(
                            selected = selectedFormat == format,
                            onClick = { onFormatChange(format) },
                            label = { Text(format.toLabel()) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(Res.string.filter_reset))
                }

                Button(
                    onClick = {
                        onApply()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(Res.string.filter_apply))
                }
            }
        }
    }
}
