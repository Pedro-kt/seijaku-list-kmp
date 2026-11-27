package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.FlowRow
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
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = responsive.horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
    ) {
        Text(
            text = stringResource(Res.string.detail_watch_on),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
            verticalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            links.forEach { link ->
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

    FilterChip(
        selected = false,
        onClick = onClick,
        label = {
            Text(
                text = link.siteName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = {
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
                    modifier = Modifier.size(responsive.smallIconSize)
                )
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(responsive.minTouchTarget / 2),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurface,
            iconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = false,
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderColor = MaterialTheme.colorScheme.outline,
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp
        )
    )
}
