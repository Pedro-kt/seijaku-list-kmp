package com.yumedev.seijakulistkmp.features.profile.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yumedev.seijakulistkmp.features.profile.presentation.model.EpisodeSortOption
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowsSort
import dev.seyfarth.tablericons.outlined.Check
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.sort_by
import seijakulistkmp.shared.generated.resources.sort_by_rating
import seijakulistkmp.shared.generated.resources.sort_alphabetically

@Composable
fun EpisodeSortDropdown(
    currentSort: EpisodeSortOption,
    onSortChanged: (EpisodeSortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = TablerIcons.Outlined.ArrowsSort,
                contentDescription = stringResource(Res.string.sort_by),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(Res.string.sort_by_rating))
                },
                onClick = {
                    onSortChanged(EpisodeSortOption.RATING)
                    expanded = false
                },
                leadingIcon = {
                    if (currentSort == EpisodeSortOption.RATING) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )

            DropdownMenuItem(
                text = {
                    Text(text = stringResource(Res.string.sort_alphabetically))
                },
                onClick = {
                    onSortChanged(EpisodeSortOption.ALPHABETICALLY)
                    expanded = false
                },
                leadingIcon = {
                    if (currentSort == EpisodeSortOption.ALPHABETICALLY) {
                        Icon(
                            imageVector = TablerIcons.Outlined.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
}
