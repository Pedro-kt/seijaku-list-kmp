package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.core.design.theme.ResponsiveTheme
import com.yumedev.seijakulistkmp.features.search.presentation.model.CharacterResultItem

@Composable
fun CharacterResultItem(
    item: CharacterResultItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = responsive.horizontalPadding, vertical = responsive.smallSpacing),
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .width(85.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(responsive.smallSpacing)),
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
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (item.mediaAppearances.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
                        ) {
                            item.mediaAppearances.take(2).forEach { appearance ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(responsive.smallPadding),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = appearance.mediaTitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )

                                    if (appearance.role != null) {
                                        RoleBadge(role = appearance.role)
                                    }
                                }
                            }
                        }
                    }
                }

                if (item.mediaAppearances.size > 2) {
                    Text(
                        text = "+${item.mediaAppearances.size - 2} more appearances",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleBadge(
    role: String,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, label) = when (role) {
        "MAIN" -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "Main"
        )
        "SUPPORTING" -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "Supporting"
        )
        "BACKGROUND" -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Background"
        )
        else -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            role
        )
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = ResponsiveTheme.values.smallSpacing, vertical = ResponsiveTheme.values.smallPadding)
        )
    }
}
