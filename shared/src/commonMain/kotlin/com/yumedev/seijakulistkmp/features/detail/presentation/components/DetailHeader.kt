package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Star
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun DetailHeader(
    coverImageUrl: String?,
    type: String?,
    demographic: String?,
    title: String,
    titleNative: String?,
    year: Int?,
    format: String?,
    chapters: Int?,
    volumes: Int?,
    episodes: Int?,
    status: String?,
    averageScore: Double?,
    totalVotes: Int?,
    rankingPosition: Int?,
    popularityPosition: Int?,
    onAddToListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            coverImageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (type != null || demographic != null) {
            Text(
                text = buildString {
                    if (type != null) append(type.uppercase())
                    if (type != null && demographic != null) append(" · ")
                    if (demographic != null) append(demographic.uppercase())
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        titleNative?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = buildMetadataString(
                year = year,
                format = format,
                chapters = chapters,
                volumes = volumes,
                episodes = episodes,
                status = status
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            averageScore?.let { score ->
                StatItem(
                    icon = {
                        Icon(
                            imageVector = TablerIcons.Filled.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    value = formatScore(score),
                    label = formatVotes(totalVotes),
                    modifier = Modifier.weight(1f)
                )
            }

            rankingPosition?.let { rank ->
                StatItem(
                    value = "#$rank",
                    label = stringResource(Res.string.detail_ranking),
                    modifier = Modifier.weight(1f)
                )
            }

            popularityPosition?.let { popularity ->
                StatItem(
                    value = "#$popularity",
                    label = stringResource(Res.string.detail_popularity),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onAddToListClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(
                text = stringResource(Res.string.detail_add_to_list),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

private fun buildMetadataString(
    year: Int?,
    format: String?,
    chapters: Int?,
    volumes: Int?,
    episodes: Int?,
    status: String?
): String {
    return buildList {
        year?.let { add(it.toString()) }
        format?.let { add(it) }
        chapters?.let { add("$it cap") }
        volumes?.let { add("$it vol") }
        episodes?.let { add("$it ep") }
        status?.let { add(it) }
    }.joinToString(" · ")
}

private fun formatScore(score: Double): String {
    val formatted = (score / 10.0 * 100).toInt() / 100.0
    return formatted.toString().take(4).trimEnd('.')
}

private fun formatVotes(votes: Int?): String {
    votes ?: return ""
    return when {
        votes >= 1_000_000 -> {
            val m = (votes / 100_000.0).toInt() / 10.0
            "${m}M votos"
        }
        votes >= 1_000 -> {
            val k = (votes / 100.0).toInt() / 10.0
            "${k}K votos"
        }
        else -> "$votes votos"
    }
}
