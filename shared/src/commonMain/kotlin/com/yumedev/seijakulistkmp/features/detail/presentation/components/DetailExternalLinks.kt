package com.yumedev.seijakulistkmp.features.detail.presentation.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.features.detail.domain.model.ExternalLink
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ExternalLink
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun DetailExternalLinks(
    links: List<ExternalLink>,
    onLinkClick: (ExternalLink) -> Unit,
    modifier: Modifier = Modifier
) {
    if (links.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.detail_watch_on),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(links) { link ->
                ExternalLinkChip(
                    link = link,
                    onClick = { onLinkClick(link) }
                )
            }
        }
    }
}

@Composable
private fun ExternalLinkChip(
    link: ExternalLink,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultColor = MaterialTheme.colorScheme.primary
    val backgroundColor = link.color?.let { colorString ->
        parseHexColor(colorString) ?: defaultColor
    } ?: defaultColor

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor.copy(alpha = 0.12f),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon or logo
            if (!link.icon.isNullOrBlank()) {
                AsyncImage(
                    model = link.icon,
                    contentDescription = link.site,
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Icon(
                    imageVector = TablerIcons.Outlined.ExternalLink,
                    contentDescription = null,
                    tint = backgroundColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Site name
            Text(
                text = link.site,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = backgroundColor
            )
        }
    }
}

private fun parseHexColor(colorString: String): Color? {
    return try {
        val hex = colorString.removePrefix("#")
        val colorInt = hex.toLongOrNull(16) ?: return null
        Color(colorInt or 0xFF000000)
    } catch (e: Exception) {
        null
    }
}
