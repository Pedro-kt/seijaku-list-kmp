package com.yumedev.seijakulistkmp.features.tracking.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.tracking.domain.model.MediaListSortOption
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowDown
import dev.seyfarth.tablericons.outlined.ArrowUp
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    selectedSortOption: MediaListSortOption,
    ascending: Boolean,
    onSortOptionSelected: (MediaListSortOption, Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(Res.string.list_sort_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup()
            ) {
                SortOption(
                    label = stringResource(Res.string.list_sort_by_title),
                    sortOption = MediaListSortOption.TITLE,
                    selectedSortOption = selectedSortOption,
                    onSelect = { onSortOptionSelected(it, ascending) }
                )

                SortOption(
                    label = stringResource(Res.string.list_sort_by_score),
                    sortOption = MediaListSortOption.SCORE,
                    selectedSortOption = selectedSortOption,
                    onSelect = { onSortOptionSelected(it, ascending) }
                )

                SortOption(
                    label = stringResource(Res.string.list_sort_by_progress),
                    sortOption = MediaListSortOption.PROGRESS,
                    selectedSortOption = selectedSortOption,
                    onSelect = { onSortOptionSelected(it, ascending) }
                )

                SortOption(
                    label = stringResource(Res.string.list_sort_by_updated),
                    sortOption = MediaListSortOption.UPDATED_AT,
                    selectedSortOption = selectedSortOption,
                    onSelect = { onSortOptionSelected(it, ascending) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = ascending,
                    onClick = { onSortOptionSelected(selectedSortOption, true) },
                    label = { Text(stringResource(Res.string.list_sort_ascending)) },
                    leadingIcon = {
                        Icon(
                            imageVector = TablerIcons.Outlined.ArrowUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                FilterChip(
                    selected = !ascending,
                    onClick = { onSortOptionSelected(selectedSortOption, false) },
                    label = { Text(stringResource(Res.string.list_sort_descending)) },
                    leadingIcon = {
                        Icon(
                            imageVector = TablerIcons.Outlined.ArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SortOption(
    label: String,
    sortOption: MediaListSortOption,
    selectedSortOption: MediaListSortOption,
    onSelect: (MediaListSortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = sortOption == selectedSortOption,
                onClick = { onSelect(sortOption) },
                role = Role.RadioButton
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = sortOption == selectedSortOption,
            onClick = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
