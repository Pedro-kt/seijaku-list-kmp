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
import com.yumedev.seijakulistkmp.features.detail.presentation.model.ExternalLinkUiModel
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ExternalLink
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun DetailExternalLinks(
    links: List<ExternalLinkUiModel>,
    onLinkClick: (String?) -> Unit,

    modifier: Modifier = Modifier
) {
    if (links.isEmpty()) return

    val responsive = ResponsiveTheme.values
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
    ) {
        Text(
            text = stringResource(Res.string.detail_watch_on),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = responsive.horizontalPadding)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
            contentPadding = PaddingValues(horizontal = responsive.horizontalPadding)
        ) {
            items(links) { link ->
                ExternalLinkChip(
                    link = link,
                    onClick = { onLinkClick(link.url) }
                )
            }
        }
    }
}

@Composable
private fun ExternalLinkChip(
    link: ExternalLinkUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    val backgroundColor = link.backgroundColor
    val textColor = link.textColor

    Surface(
        modifier = modifier
            .height(responsive.minTouchTarget)
            .clip(RoundedCornerShape(responsive.minTouchTarget / 2))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(responsive.minTouchTarget / 2),
        color = backgroundColor,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = responsive.mediumPadding),
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon or logo
            if (!link.iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = link.iconUrl,
                    contentDescription = link.siteName,
                    modifier = Modifier.size(responsive.smallIconSize),
                    contentScale = ContentScale.Fit
                )
            } else {
                Icon(
                    imageVector = TablerIcons.Outlined.ExternalLink,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(responsive.smallIconSize)
                )
            }

            // Site name
            Text(
                text = link.siteName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = textColor
            )
        }
    }
}
