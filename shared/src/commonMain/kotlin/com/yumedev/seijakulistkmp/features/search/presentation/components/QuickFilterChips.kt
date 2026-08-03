package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.search.presentation.model.QuickFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.toLabel
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Calendar
import dev.seyfarth.tablericons.outlined.Clock
import dev.seyfarth.tablericons.outlined.Dice
import dev.seyfarth.tablericons.outlined.Trophy

@Composable
fun QuickFilterChips(
    onFilterClick: (QuickFilter, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val currentSeasonLabel = QuickFilter.CurrentSeason.toLabel()
            QuickFilterChip(
                filter = QuickFilter.CurrentSeason,
                icon = TablerIcons.Outlined.Calendar,
                onClick = { onFilterClick(QuickFilter.CurrentSeason, currentSeasonLabel) },
                modifier = Modifier.weight(1f)
            )
            val airingTodayLabel = QuickFilter.AiringToday.toLabel()
            QuickFilterChip(
                filter = QuickFilter.AiringToday,
                icon = TablerIcons.Outlined.Clock,
                onClick = { onFilterClick(QuickFilter.AiringToday, airingTodayLabel) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val top100Label = QuickFilter.Top100.toLabel()
            QuickFilterChip(
                filter = QuickFilter.Top100,
                icon = TablerIcons.Outlined.Trophy,
                onClick = { onFilterClick(QuickFilter.Top100, top100Label) },
                modifier = Modifier.weight(1f)
            )
            val randomLabel = QuickFilter.Random.toLabel()
            QuickFilterChip(
                filter = QuickFilter.Random,
                icon = TablerIcons.Outlined.Dice,
                onClick = { onFilterClick(QuickFilter.Random, randomLabel) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickFilterChip(
    filter: QuickFilter,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = filter.toLabel(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
