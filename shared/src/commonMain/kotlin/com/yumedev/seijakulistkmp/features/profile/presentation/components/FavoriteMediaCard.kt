package com.yumedev.seijakulistkmp.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListEntry
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun FavoriteMediaCard(
    entry: MediaListEntry,
    position: Int,
    onClick: () -> Unit,
    onLongPress: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    Card(
        onClick = onClick,
        modifier = modifier
            .size(width = responsive.cardWidth * 0.5625f, height = responsive.cardImageHeight * 0.7f),
        shape = RoundedCornerShape(responsive.mediumPadding),
        elevation = CardDefaults.cardElevation(
            defaultElevation = responsive.smallPadding / 2
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            entry.mediaInfo?.coverImage?.let { coverUrl ->
                AsyncImage(
                    model = coverUrl,
                    contentDescription = entry.mediaInfo.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(responsive.largeIconSize + responsive.mediumPadding)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    )
            )

            Surface(
                modifier = Modifier
                    .padding(responsive.smallSpacing)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(responsive.smallSpacing),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "#$position",
                    modifier = Modifier.padding(horizontal = responsive.smallSpacing, vertical = responsive.smallPadding / 2),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            entry.mediaInfo?.title?.let { title ->
                Text(
                    text = title,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = responsive.smallSpacing, vertical = responsive.smallSpacing),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
