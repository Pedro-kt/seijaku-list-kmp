package com.yumedev.seijakulistkmp.features.tracking.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry

@Composable
fun MediaListCard(
    entry: MediaListEntry,
    onIncrementProgress: () -> Unit,
    onEditClick: () -> Unit,
    onStatusChange: (com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListStatus) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardType: MediaListCardType = MediaListCardType.Compact
) {
    when (cardType) {
        is MediaListCardType.Compact -> CompactMediaListCard(
            entry = entry,
            onIncrementProgress = onIncrementProgress,
            onEditClick = onEditClick,
            onStatusChange = onStatusChange,
            onDeleteClick = onDeleteClick,
            modifier = modifier
        )
        // Future card types will be handled here:
        // is MediaListCardType.Grid -> GridMediaListCard(...)
        // is MediaListCardType.Detailed -> DetailedMediaListCard(...)
    }
}
