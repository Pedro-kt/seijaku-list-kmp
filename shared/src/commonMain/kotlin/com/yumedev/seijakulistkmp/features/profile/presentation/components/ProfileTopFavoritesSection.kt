package com.yumedev.seijakulistkmp.features.profile.presentation.components

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.profile_top_anime
import seijakulistkmp.shared.generated.resources.profile_top_manga
import kotlin.math.roundToInt

@Composable
fun ProfileTopFavoritesSection(
    favorites: List<MediaListEntry>,
    isAnime: Boolean,
    onAddFavorite: (Int) -> Unit,
    onFavoriteClick: (MediaListEntry) -> Unit,
    onReorderFavorites: (Int, Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var draggedPosition by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var targetPosition by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(if (isAnime) Res.string.profile_top_anime else Res.string.profile_top_manga),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (index in 0 until 5) {
                val position = index + 1
                val favoriteAtPosition = favorites.find { it.favoritePosition == position }
                val isDragged = draggedPosition == position
                val isTarget = targetPosition == position

                Box(
                    modifier = Modifier
                        .zIndex(if (isDragged) 1f else 0f)
                        .then(
                            if (isDragged) {
                                Modifier.offset {
                                    IntOffset(
                                        dragOffset.x.roundToInt(),
                                        dragOffset.y.roundToInt()
                                    )
                                }
                            } else {
                                Modifier
                            }
                        )
                        .graphicsLayer {
                            scaleX = if (isDragged) 1.05f else if (isTarget) 0.95f else 1f
                            scaleY = if (isDragged) 1.05f else if (isTarget) 0.95f else 1f
                            alpha = if (isDragged) 0.8f else 1f
                        }
                        .then(
                            if (favoriteAtPosition != null) {
                                Modifier.pointerInput(position) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            draggedPosition = position
                                            dragOffset = Offset.Zero
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffset += dragAmount

                                            val cardWidth = 90.dp.toPx() + 12.dp.toPx()
                                            val horizontalMovement = (dragOffset.x / cardWidth).roundToInt()
                                            val newTargetPosition = (position + horizontalMovement).coerceIn(1, 5)

                                            if (newTargetPosition != targetPosition) {
                                                targetPosition = newTargetPosition
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            }
                                        },
                                        onDragEnd = {
                                            if (draggedPosition != null && targetPosition != null && draggedPosition != targetPosition) {
                                                onReorderFavorites(draggedPosition!!, targetPosition!!, isAnime)
                                            }
                                            draggedPosition = null
                                            dragOffset = Offset.Zero
                                            targetPosition = null
                                        },
                                        onDragCancel = {
                                            draggedPosition = null
                                            dragOffset = Offset.Zero
                                            targetPosition = null
                                        }
                                    )
                                }
                            } else {
                                Modifier
                            }
                        )
                ) {
                    if (favoriteAtPosition != null) {
                        FavoriteMediaCard(
                            entry = favoriteAtPosition,
                            position = position,
                            onClick = {
                                if (draggedPosition == null) {
                                    onFavoriteClick(favoriteAtPosition)
                                }
                            }
                        )
                    } else {
                        FavoriteMediaPlaceholder(
                            position = position,
                            onClick = { onAddFavorite(position) }
                        )
                    }
                }
            }
        }
    }
}
