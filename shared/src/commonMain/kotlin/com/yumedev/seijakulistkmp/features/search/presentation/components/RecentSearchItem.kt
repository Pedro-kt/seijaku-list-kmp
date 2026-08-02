package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.search.presentation.model.RecentSearch
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowRight
import dev.seyfarth.tablericons.outlined.Clock
import dev.seyfarth.tablericons.outlined.TrendingUp
import dev.seyfarth.tablericons.outlined.X

@Composable
fun RecentSearchItem(
    search: RecentSearch,
    onSearchClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
    showClockIcon: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSearchClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (showClockIcon) TablerIcons.Outlined.Clock else TablerIcons.Outlined.TrendingUp,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = search.query,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = if (showClockIcon) TablerIcons.Outlined.X else TablerIcons.Outlined.ArrowRight,
                contentDescription = if (showClockIcon) "Remove" else "Navigate",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
