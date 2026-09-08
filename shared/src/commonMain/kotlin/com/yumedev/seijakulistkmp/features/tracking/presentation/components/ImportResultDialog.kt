package com.yumedev.seijakulistkmp.features.tracking.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.tracking.domain.model.ImportResult
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.import_result_and_more
import seijakulistkmp.shared.generated.resources.import_result_close
import seijakulistkmp.shared.generated.resources.import_result_conflicts
import seijakulistkmp.shared.generated.resources.import_result_errors
import seijakulistkmp.shared.generated.resources.import_result_ok
import seijakulistkmp.shared.generated.resources.import_result_resolve_conflicts
import seijakulistkmp.shared.generated.resources.import_result_successful
import seijakulistkmp.shared.generated.resources.import_result_summary
import seijakulistkmp.shared.generated.resources.import_result_title
import seijakulistkmp.shared.generated.resources.import_result_total_processed

@Composable
fun ImportResultDialog(
    importResult: ImportResult,
    onDismiss: () -> Unit,
    onResolveConflicts: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(Res.string.import_result_title))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.import_result_summary),
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        ResultRow(label = stringResource(Res.string.import_result_total_processed), value = importResult.totalProcessed.toString())
                        ResultRow(label = stringResource(Res.string.import_result_successful), value = importResult.successCount.toString())
                        ResultRow(label = stringResource(Res.string.import_result_conflicts), value = importResult.conflictCount.toString())
                        ResultRow(label = stringResource(Res.string.import_result_errors), value = importResult.errorCount.toString())
                    }
                }

                if (importResult.hasErrors) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.import_result_errors),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            importResult.errors.take(3).forEach { error ->
                                Text(
                                    text = "• ${error.message}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (importResult.errors.size > 3) {
                                Text(
                                    text = stringResource(Res.string.import_result_and_more, importResult.errors.size - 3),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (importResult.hasConflicts) {
                Button(onClick = onResolveConflicts) {
                    Text(stringResource(Res.string.import_result_resolve_conflicts))
                }
            } else {
                Button(onClick = onDismiss) {
                    Text(stringResource(Res.string.import_result_ok))
                }
            }
        },
        dismissButton = if (importResult.hasConflicts) {
            {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.import_result_close))
                }
            }
        } else null
    )
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
