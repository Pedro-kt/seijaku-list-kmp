package com.yumedev.seijakulistkmp.features.tracking.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.core.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeStatusBottomSheet(
    currentStatus: MediaListStatus,
    mediaType: MediaType,
    onDismiss: () -> Unit,
    onStatusSelected: (MediaListStatus) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(Res.string.list_change_status_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            Divider()

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(getStatusOptions(mediaType)) { status ->
                    StatusOption(
                        status = status,
                        mediaType = mediaType,
                        isSelected = status == currentStatus,
                        onClick = {
                            onStatusSelected(status)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusOption(
    status: MediaListStatus,
    mediaType: MediaType,
    isSelected: Boolean,
    onClick: () -> Unit
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyLarge
        )

        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }
}

private fun getStatusOptions(mediaType: MediaType): List<MediaListStatus> {
    return listOf(
        MediaListStatus.CURRENT,
        MediaListStatus.COMPLETED,
        MediaListStatus.PLANNING,
        MediaListStatus.PAUSED,
        MediaListStatus.DROPPED,
        MediaListStatus.REPEATING
    )
}
