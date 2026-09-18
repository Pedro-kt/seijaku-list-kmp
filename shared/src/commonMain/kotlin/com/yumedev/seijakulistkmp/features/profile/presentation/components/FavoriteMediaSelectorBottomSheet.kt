package com.yumedev.seijakulistkmp.features.profile.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import com.yumedev.seijakulistkmp.features.tracking.presentation.components.ExpressiveFilterChip
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.all
import seijakulistkmp.shared.generated.resources.list_status_completed
import seijakulistkmp.shared.generated.resources.list_status_dropped
import seijakulistkmp.shared.generated.resources.list_status_paused
import seijakulistkmp.shared.generated.resources.list_status_plan_to_watch
import seijakulistkmp.shared.generated.resources.list_status_repeating
import seijakulistkmp.shared.generated.resources.list_status_watching
import seijakulistkmp.shared.generated.resources.profile_no_entries_found
import seijakulistkmp.shared.generated.resources.profile_score_format
import seijakulistkmp.shared.generated.resources.profile_select_favorite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteMediaSelectorBottomSheet(
    allEntries: List<MediaListEntry>,
    excludedMediaIds: Set<Int>,
    position: Int,
    onDismiss: () -> Unit,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedStatus by remember { mutableStateOf<MediaListStatus?>(null) }

    val filteredEntries = remember(allEntries, excludedMediaIds, selectedStatus) {
        allEntries
            .filter { it.mediaId !in excludedMediaIds }
            .filter { selectedStatus == null || it.status == selectedStatus }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.profile_select_favorite),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                item {
                    ExpressiveFilterChip(
                        selected = selectedStatus == null,
                        onClick = { selectedStatus = null },
                        label = { Text(stringResource(Res.string.all)) }
                    )
                }

                val statusFilters = listOf(
                    MediaListStatus.CURRENT to Res.string.list_status_watching,
                    MediaListStatus.COMPLETED to Res.string.list_status_completed,
                    MediaListStatus.PLANNING to Res.string.list_status_plan_to_watch,
                    MediaListStatus.PAUSED to Res.string.list_status_paused,
                    MediaListStatus.DROPPED to Res.string.list_status_dropped,
                    MediaListStatus.REPEATING to Res.string.list_status_repeating
                )

                items(statusFilters) { (status, labelRes) ->
                    ExpressiveFilterChip(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status },
                        label = { Text(stringResource(labelRes)) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = filteredEntries,
                    key = { it.mediaId }
                ) { entry ->
                    MediaSelectorItem(
                        entry = entry,
                        onClick = { onSelect(entry.mediaId) }
                    )
                }

                if (filteredEntries.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.profile_no_entries_found),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaSelectorItem(
    entry: MediaListEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        entry.mediaInfo?.coverImage?.let { coverUrl ->
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(width = 50.dp, height = 70.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )
        } ?: Spacer(modifier = Modifier.size(width = 50.dp, height = 70.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            entry.mediaInfo?.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            entry.score?.let { score ->
                Text(
                    text = stringResource(Res.string.profile_score_format, score.toString()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
