package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

data class InfoItem(
    val label: String,
    val value: String
)

@Composable
fun DetailInformation(
    type: MediaType,
    format: String?,
    volumes: Int?,
    status: String?,
    chapters: Int?,
    episodes: Int?,
    startDate: String?,
    endDate: String?,
    serialization: String?,
    author: String?,
    artist: String?,
    studio: String?,
    demographic: String?,
    license: String?,
    modifier: Modifier = Modifier
) {
    val infoItems = buildInfoItems(
        type = type,
        format = format,
        volumes = volumes,
        status = status,
        chapters = chapters,
        episodes = episodes,
        startDate = startDate,
        endDate = endDate,
        serialization = serialization,
        author = author,
        artist = artist,
        studio = studio,
        demographic = demographic,
        license = license
    )

    if (infoItems.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.detail_information),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                infoItems.chunked(2).forEachIndexed { index, rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        ) {
                            InfoItemView(
                                label = rowItems[0].label,
                                value = rowItems[0].value,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (rowItems.size == 2) {
                            VerticalDivider(
                                modifier = Modifier.fillMaxHeight(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(16.dp)
                            ) {
                                InfoItemView(
                                    label = rowItems[1].label,
                                    value = rowItems[1].value,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    if (index < infoItems.chunked(2).size - 1) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItemView(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun buildInfoItems(
    type: MediaType,
    format: String?,
    volumes: Int?,
    status: String?,
    chapters: Int?,
    episodes: Int?,
    startDate: String?,
    endDate: String?,
    serialization: String?,
    author: String?,
    artist: String?,
    studio: String?,
    demographic: String?,
    license: String?
): List<InfoItem> {
    return buildList {
        // Type
        format?.let {
            add(InfoItem(
                label = stringResource(Res.string.detail_info_type),
                value = it
            ))
        }

        // Volumes (for manga)
        if (type == MediaType.MANGA) {
            volumes?.let {
                add(InfoItem(
                    label = stringResource(Res.string.detail_info_volumes),
                    value = it.toString()
                ))
            }
        }

        // Status
        status?.let {
            add(InfoItem(
                label = stringResource(Res.string.detail_info_status),
                value = getStatusText(it)
            ))
        }

        // Chapters or Episodes
        if (type == MediaType.MANGA) {
            chapters?.let {
                add(InfoItem(
                    label = stringResource(Res.string.detail_info_chapters),
                    value = it.toString()
                ))
            }
        } else {
            episodes?.let {
                add(InfoItem(
                    label = stringResource(Res.string.detail_info_episodes),
                    value = it.toString()
                ))
            }
        }

        if (startDate != null || endDate != null) {
            val dateRange = buildString {
                append(startDate ?: "?")
                append(" – ")
                append(endDate ?: "presente")
            }
            add(InfoItem(
                label = stringResource(
                    if (type == MediaType.MANGA) Res.string.detail_info_publication
                    else Res.string.detail_info_broadcast
                ),
                value = dateRange
            ))
        }

        if (type == MediaType.MANGA) {
            serialization?.let {
                add(InfoItem(
                    label = stringResource(Res.string.detail_info_serialization),
                    value = it
                ))
            }
        }

        author?.let {
            add(InfoItem(
                label = stringResource(Res.string.detail_info_author),
                value = it
            ))
        }

        if (type == MediaType.MANGA) {
            artist?.let {
                add(InfoItem(
                    label = stringResource(Res.string.detail_info_art),
                    value = it
                ))
            }
        } else {
            studio?.let {
                add(InfoItem(
                    label = stringResource(Res.string.detail_info_studio),
                    value = it
                ))
            }
        }

        demographic?.let {
            add(InfoItem(
                label = stringResource(Res.string.detail_info_demographic),
                value = it
            ))
        }

        license?.let {
            add(InfoItem(
                label = stringResource(Res.string.detail_info_license),
                value = it
            ))
        }
    }
}

@Composable
private fun getStatusText(status: String): String {
    return when (status.uppercase()) {
        "FINISHED" -> stringResource(Res.string.status_finished)
        "RELEASING" -> stringResource(Res.string.status_airing)
        "NOT_YET_RELEASED" -> stringResource(Res.string.status_not_yet_aired)
        "CANCELLED" -> stringResource(Res.string.status_cancelled)
        "HIATUS" -> stringResource(Res.string.status_cancelled)
        else -> status
    }
}
