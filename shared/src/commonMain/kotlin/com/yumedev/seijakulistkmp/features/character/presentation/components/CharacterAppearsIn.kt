package com.yumedev.seijakulistkmp.features.character.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.features.character.presentation.model.MediaAppearanceUiModel
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun CharacterAppearsIn(
    media: List<MediaAppearanceUiModel>,
    onMediaClick: (Int, MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    if (media.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
    ) {
        Text(
            text = stringResource(Res.string.character_appears_in),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = responsive.horizontalPadding)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding),
            contentPadding = PaddingValues(horizontal = responsive.horizontalPadding)
        ) {
            items(media) { mediaItem ->
                MediaAppearanceCard(
                    media = mediaItem,
                    onClick = { onMediaClick(mediaItem.id, mediaItem.type) }
                )
            }
        }
    }
}

@Composable
private fun MediaAppearanceCard(
    media: MediaAppearanceUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    Column(
        modifier = modifier.width(responsive.cardWidth * 0.875f),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumSpacing)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(responsive.cardImageHeight)
                .clip(RoundedCornerShape(responsive.cornerRadius))
                .clickable(onClick = onClick)
        ) {
            AsyncImage(
                model = media.coverImageUrl,
                contentDescription = media.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(responsive.largeIconSize + responsive.mediumIconSize)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            Text(
                text = media.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(responsive.smallPadding * 2.5f)
            )
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(responsive.smallPadding),
            verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
        ) {
            val parts = media.typeLabel.split(" · ")

            if (parts.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = parts[0],
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = responsive.smallPadding * 2.5f, vertical = responsive.smallPadding)
                    )
                }
            }

            if (parts.size > 1) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = parts[1],
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = responsive.smallPadding * 2.5f, vertical = responsive.smallPadding)
                    )
                }
            }
        }
    }
}
