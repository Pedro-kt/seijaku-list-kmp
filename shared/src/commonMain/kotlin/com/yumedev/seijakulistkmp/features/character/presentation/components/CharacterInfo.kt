package com.yumedev.seijakulistkmp.features.character.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.core.clipboard.rememberClipboardManager
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Copy
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun CharacterInfo(
    age: String?,
    height: String?,
    birthday: String?,
    nickname: String?,
    modifier: Modifier = Modifier
) {
    val hasAnyInfo = age != null || height != null || birthday != null || nickname != null
    if (!hasAnyInfo) return

    val clipboardManager = rememberClipboardManager()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        data class InfoItem(val label: String, val value: String, val showCopyIcon: Boolean = false)

        val infoItems = buildList {
            age?.let { add(InfoItem(stringResource(Res.string.character_age), it)) }
            height?.let { add(InfoItem(stringResource(Res.string.character_height), it)) }
            birthday?.let { add(InfoItem(stringResource(Res.string.character_birthday), it)) }
            nickname?.let { add(InfoItem(stringResource(Res.string.character_nickname), it, showCopyIcon = true)) }
        }

        infoItems.forEachIndexed { index, item ->
            InfoRow(
                label = item.label,
                value = item.value,
                showCopyIcon = item.showCopyIcon,
                onCopyClick = { clipboardManager.copyToClipboard(item.value) }
            )

            if (index < infoItems.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    showCopyIcon: Boolean = false,
    onCopyClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            if (showCopyIcon) {
                IconButton(
                    onClick = onCopyClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = TablerIcons.Outlined.Copy,
                        contentDescription = "Copiar $label",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
