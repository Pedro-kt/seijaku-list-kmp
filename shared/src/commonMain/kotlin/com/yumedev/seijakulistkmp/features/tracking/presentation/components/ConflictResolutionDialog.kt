package com.yumedev.seijakulistkmp.features.tracking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ConflictResolution
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportConflict
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.conflict_resolution_already_exist
import seijakulistkmp.shared.generated.resources.conflict_resolution_cancel
import seijakulistkmp.shared.generated.resources.conflict_resolution_choose_action
import seijakulistkmp.shared.generated.resources.conflict_resolution_conflicts_found
import seijakulistkmp.shared.generated.resources.conflict_resolution_imported
import seijakulistkmp.shared.generated.resources.conflict_resolution_keep_local
import seijakulistkmp.shared.generated.resources.conflict_resolution_local
import seijakulistkmp.shared.generated.resources.conflict_resolution_progress
import seijakulistkmp.shared.generated.resources.conflict_resolution_status
import seijakulistkmp.shared.generated.resources.conflict_resolution_title
import seijakulistkmp.shared.generated.resources.conflict_resolution_use_imported
import seijakulistkmp.shared.generated.resources.unknown

@Composable
fun ConflictResolutionDialog(
    conflicts: List<ImportConflict>,
    onDismiss: () -> Unit,
    onResolveAll: (ConflictResolution) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(Res.string.conflict_resolution_title))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.conflict_resolution_conflicts_found, conflicts.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(Res.string.conflict_resolution_already_exist),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(conflicts) { conflict ->
                        ConflictCard(conflict = conflict)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.conflict_resolution_choose_action),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onResolveAll(ConflictResolution.UseImported) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.conflict_resolution_use_imported))
                }
                OutlinedButton(
                    onClick = { onResolveAll(ConflictResolution.KeepLocal) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.conflict_resolution_keep_local))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.conflict_resolution_cancel))
            }
        }
    )
}

@Composable
private fun ConflictCard(conflict: ImportConflict) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = conflict.localEntry.mediaInfo?.title ?: stringResource(Res.string.unknown),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.conflict_resolution_local),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(Res.string.conflict_resolution_progress, conflict.localEntry.progress),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = stringResource(Res.string.conflict_resolution_status, conflict.localEntry.status.name),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = stringResource(Res.string.conflict_resolution_imported),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(Res.string.conflict_resolution_progress, conflict.importedEntry.progress),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = stringResource(Res.string.conflict_resolution_status, conflict.importedEntry.status.name),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
