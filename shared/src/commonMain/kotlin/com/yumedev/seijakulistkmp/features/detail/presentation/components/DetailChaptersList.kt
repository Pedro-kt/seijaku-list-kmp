package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.detail.domain.model.Chapter
import com.yumedev.seijakulistkmp.features.detail.domain.model.Episode
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Star
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun DetailChaptersList(
    type: MediaType,
    chapters: List<Chapter>?,
    episodes: List<Episode>?,
    totalCount: Int?,
    onSeeAllClick: () -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val hasContent = when (type) {
        MediaType.MANGA -> !chapters.isNullOrEmpty()
        MediaType.ANIME -> !episodes.isNullOrEmpty()
    }

    if (!hasContent) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (type) {
                MediaType.MANGA -> {
                    chapters?.take(3)?.forEach { chapter ->
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
                    episodes?.take(3)?.forEach { episode ->
                        ChapterItem(
                            number = episode.number,
                            title = episode.title,
                            metadata = buildString {
                                episode.duration?.let { append("${it}min") }
                                episode.airDate?.let {
                                    if (episode.duration != null) append(" · ")
                                    append(it)
                                }
                            },
                            rating = episode.rating,
                            onClick = { onItemClick(episode.number) }
                        )
                    }
                }
            }
        }
        totalCount?.let { count ->
            if (count > 3) {
                OutlinedButton(
                    onClick = onSeeAllClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = stringResource(
                            if (type == MediaType.MANGA) Res.string.detail_see_all_chapters
                            else Res.string.detail_see_all_episodes,
                            count
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
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
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
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
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = TablerIcons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
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
