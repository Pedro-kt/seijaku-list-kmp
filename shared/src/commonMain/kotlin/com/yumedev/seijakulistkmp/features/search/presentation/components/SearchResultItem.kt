package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.features.search.presentation.model.MediaType
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchResultItem
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Bookmark
import dev.seyfarth.tablericons.filled.Star
import dev.seyfarth.tablericons.outlined.Bookmark
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun SearchResultItem(
    item: SearchResultItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSaveClick: ((SearchResultItem) -> Unit)? = null,
    isSaved: Boolean = false
) {
    val responsive = ResponsiveTheme.values
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(responsive.minTouchTarget * 3)
            .scale(scale),
        color = MaterialTheme.colorScheme.surface,
        interactionSource = interactionSource
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = responsive.horizontalPadding, vertical = responsive.mediumSpacing),
                horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
            ) {
                AsyncImage(
                    model = item.coverImage,
                    contentDescription = item.title,
                    modifier = Modifier
                        .width(responsive.cardWidth / 2)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(responsive.mediumSpacing)),
                    contentScale = ContentScale.Crop
                )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        if (item.status != null) {
                            Spacer(modifier = Modifier.width(responsive.mediumSpacing))
                            StatusBadge(status = item.status)
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(responsive.smallSpacing),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.rating != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = TablerIcons.Filled.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(responsive.smallIconSize),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "%.2f".format(item.rating),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "|",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (item.year != null) {
                            Text(
                                text = item.year.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "·",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = item.type,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        when (item.mediaType) {
                            MediaType.ANIME -> {
                                if (item.episodes != null) {
                                    Text(
                                        text = "·",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${item.episodes} ep",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            MediaType.MANGA -> {
                                if (item.chapters != null) {
                                    Text(
                                        text = "·",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${item.chapters} ch",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    if (!item.alternativeTitle.isNullOrBlank()) {
                        Text(
                            text = item.alternativeTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (item.genres.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(responsive.smallSpacing),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item.genres.take(2).forEach { genre ->
                            Surface(
                                shape = RoundedCornerShape(responsive.smallPadding),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = genre,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = responsive.smallSpacing, vertical = 2.dp)
                                )
                            }
                        }

                        if (item.genres.size > 2) {
                            Surface(
                                shape = RoundedCornerShape(responsive.smallPadding),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "+${item.genres.size - 2}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = responsive.smallSpacing, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (onSaveClick != null) {
            FilledIconButton(
                onClick = { onSaveClick(item) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = responsive.horizontalPadding, bottom = responsive.mediumSpacing)
                    .size(responsive.mediumIconSize),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = if (isSaved) TablerIcons.Filled.Bookmark else TablerIcons.Outlined.Bookmark,
                    contentDescription = if (isSaved) "Remove from saved" else "Save for later",
                    modifier = Modifier.size(responsive.smallIconSize)
                )
            }
        }
    }
    }
}

@Composable
private fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    val (backgroundColor, textColor, label) = when (status) {
        "RELEASING" -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "Airing"
        )
        "FINISHED" -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Finished"
        )
        "NOT_YET_RELEASED" -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "Upcoming"
        )
        "CANCELLED" -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "Cancelled"
        )
        else -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            status
        )
    }

    Surface(
        shape = RoundedCornerShape(responsive.smallPadding),
        color = backgroundColor,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = responsive.smallSpacing, vertical = 2.dp)
        )
    }
}
