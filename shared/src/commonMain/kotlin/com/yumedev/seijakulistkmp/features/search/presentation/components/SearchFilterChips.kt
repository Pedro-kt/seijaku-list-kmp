package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.search.presentation.model.SearchFilter
import com.yumedev.seijakulistkmp.features.search.presentation.model.toLabel
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.AdjustmentsHorizontal
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun SearchFilterChips(
    selectedFilter: SearchFilter,
    onFilterSelect: (SearchFilter) -> Unit,
    onFiltersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Filters button
        Surface(
            onClick = onFiltersClick,
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = TablerIcons.Outlined.AdjustmentsHorizontal,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(Res.string.search_filters),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Filter type chips
        SearchFilter.entries.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelect(filter) },
                label = {
                    Text(
                        text = filter.toLabel(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
