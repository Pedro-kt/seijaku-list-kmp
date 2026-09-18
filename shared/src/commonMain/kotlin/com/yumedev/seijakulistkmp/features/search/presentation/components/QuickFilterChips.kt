package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.search.presentation.model.QuickFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.toLabel
import com.yumedev.seijakulistkmp.ui.theme.SeijakuTheme
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 6f else 2f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isPressed) 1.15f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )

    Surface(
        onClick = onClick,
        shape = SeijakuTheme.shapes.buttonSecondary,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = elevation.dp,
        modifier = modifier.scale(scale),
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .scale(iconScale),
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
