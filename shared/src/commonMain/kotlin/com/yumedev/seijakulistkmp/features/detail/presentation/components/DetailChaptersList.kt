package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.model.ChapterUiModel
import com.yumedev.seijakulistkmp.features.detail.presentation.model.EpisodeUiModel
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Star
import dev.seyfarth.tablericons.outlined.PlayerPlay
import dev.seyfarth.tablericons.outlined.Star
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun DetailChaptersList(
    type: MediaType,
    chapters: List<ChapterUiModel>?,
    episodes: List<EpisodeUiModel>?,
    totalCount: Int?,
    onSeeAllClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    onEpisodeFavoriteToggle: (episodeNumber: Int, episodeTitle: String, thumbnailUrl: String?) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val hasContent = when (type) {
        MediaType.MANGA -> !chapters.isNullOrEmpty()
        MediaType.ANIME -> !episodes.isNullOrEmpty()
    }

    if (!hasContent) return

    val responsive = ResponsiveTheme.values
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = responsive.horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(
                    if (type == MediaType.MANGA) Res.string.detail_chapters
                    else Res.string.detail_episodes
                ),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            totalCount?.let { count ->
                Text(
                    text = stringResource(
                        if (type == MediaType.MANGA) Res.string.detail_chapters_count
                        else Res.string.detail_episodes_count,
                        count
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
        ) {
            when (type) {
                MediaType.MANGA -> {
                    val itemsToShow = if (isExpanded) chapters else chapters?.take(3)
                    itemsToShow?.forEach { chapter ->
                        ChapterItem(
                            number = chapter.number,
                            title = chapter.title,
                            metadata = buildString {
                                chapter.volume?.let { append("Vol. $it") }
                                chapter.releaseDate?.let {
                                    if (chapter.volume != null) append(" · ")
                                    append(it)
                                }
                            },
                            rating = chapter.rating,
                            onClick = { onItemClick(chapter.number) }
                        )
                    }
                }
                MediaType.ANIME -> {
                    val itemsToShow = if (isExpanded) episodes else episodes?.take(3)
                    itemsToShow?.forEach { episode ->
                        EpisodeItem(
                            episode = episode,
                            onClick = { onItemClick(episode.number) },
                            onFavoriteClick = {
                                onEpisodeFavoriteToggle(
                                    episode.number,
                                    episode.title,
                                    episode.thumbnail
                                )
                            }
                        )
                    }
                }
            }
        }
        totalCount?.let { count ->
            if (count > 3) {
                OutlinedButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(responsive.buttonHeight / 2)
                ) {
                    Text(
                        text = if (isExpanded) {
                            stringResource(Res.string.detail_see_less)
                        } else {
                            stringResource(
                                if (type == MediaType.MANGA) Res.string.detail_see_all_chapters
                                else Res.string.detail_see_all_episodes,
                                count
                            )
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun EpisodeItem(
    episode: EpisodeUiModel,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(responsive.cornerRadius))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(responsive.cardWidth * 0.75f)
                    .height(responsive.minTouchTarget * 1.5f)
                    .clip(RoundedCornerShape(responsive.mediumSpacing))
            ) {
                if (!episode.thumbnail.isNullOrBlank()) {
                    AsyncImage(
                        model = episode.thumbnail,
                        contentDescription = episode.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(responsive.mediumSpacing)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = episode.number.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
            ) {
                // Title
                Text(
                    text = episode.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(responsive.smallPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    episode.site?.let { site ->
                        Surface(
                            shape = RoundedCornerShape(responsive.smallPadding),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = site,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = responsive.smallSpacing, vertical = 2.dp)
                            )
                        }
                    }
                    episode.duration?.let { duration ->
                        Text(
                            text = "${duration}min",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            IconButton(
                onClick = onFavoriteClick
            ) {
                Icon(
                    imageVector = if (episode.isFavorite) TablerIcons.Filled.Star else TablerIcons.Outlined.Star,
                    contentDescription = if (episode.isFavorite) {
                        stringResource(Res.string.detail_unfavorite_episode)
                    } else {
                        stringResource(Res.string.detail_favorite_episode)
                    },
                    tint = if (episode.isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    }
                )
            }
        }
    }
}

@Composable
private fun ChapterItem(
    number: Int,
    title: String,
    metadata: String,
    rating: Double?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(responsive.mediumSpacing),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(responsive.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(responsive.mediumSpacing),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = responsive.mediumPadding, vertical = responsive.mediumSpacing)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = responsive.mediumPadding),
                verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (metadata.isNotBlank()) {
                    Text(
                        text = metadata,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            rating?.let { score ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(responsive.smallPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = TablerIcons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(responsive.smallIconSize),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${(score * 10).toInt() / 10.0}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
