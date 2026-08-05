package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.detail.presentation.model.InformationItemUiModel
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun DetailInformation(
    items: List<InformationItemUiModel>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.detail_information),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                items.chunked(2).forEachIndexed { index, rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        ) {
                            InfoItemView(
                                label = rowItems[0].label,
                                value = rowItems[0].value,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (rowItems.size == 2) {
                            VerticalDivider(
                                modifier = Modifier.fillMaxHeight(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(16.dp)
                            ) {
                                InfoItemView(
                                    label = rowItems[1].label,
                                    value = rowItems[1].value,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    if (index < items.chunked(2).size - 1) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItemView(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

