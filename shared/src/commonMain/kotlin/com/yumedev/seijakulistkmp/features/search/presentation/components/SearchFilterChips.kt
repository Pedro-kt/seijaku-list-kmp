package com.yumedev.seijakulistkmp.features.search.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(SearchFilter.entries) { filter ->
            val isSelected = selectedFilter == filter

            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = isSelected,
                    onClick = { onFilterSelect(filter) },
                    label = {
                        Text(
                            text = filter.toLabel(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    shape = if (isSelected && filter != SearchFilter.CHARACTERS) {
                        RoundedCornerShape(
                            topStart = 20.dp,
                            bottomStart = 20.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        )
                    } else {
                        RoundedCornerShape(20.dp)
                    }
                )

                if (isSelected && filter != SearchFilter.CHARACTERS) {
                    FilterChip(
                        selected = false,
                        onClick = onFiltersClick,
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = TablerIcons.Outlined.AdjustmentsHorizontal,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = stringResource(Res.string.search_filters),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                }
            }
        }
    }
}
