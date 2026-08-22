package com.yumedev.seijakulistkmp.features.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.*
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun SettingsDataSection(
    lastSyncTime: String?,
    cacheSize: String,
    onSyncClick: () -> Unit,
    onDownloadListClick: () -> Unit,
    onExportAnimeClick: () -> Unit,
    onExportMangaClick: () -> Unit,
    onClearCacheClick: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.settings_data),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        SettingsNavigationItem(
            icon = TablerIcons.Outlined.Refresh,
            title = stringResource(Res.string.settings_sync_anilist),
            description = lastSyncTime?.let {
                stringResource(Res.string.settings_sync_anilist_desc, it)
            },
            onClick = onSyncClick
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        SettingsNavigationItem(
            icon = TablerIcons.Outlined.Download,
            title = stringResource(Res.string.settings_download_list),
            description = stringResource(Res.string.settings_download_list_desc),
            onClick = onDownloadListClick
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        SettingsNavigationItem(
            icon = TablerIcons.Outlined.FileDownload,
            title = stringResource(Res.string.settings_export_anime),
            description = stringResource(Res.string.settings_export_anime_desc),
            onClick = onExportAnimeClick
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        SettingsNavigationItem(
            icon = TablerIcons.Outlined.FileDownload,
            title = stringResource(Res.string.settings_export_manga),
            description = stringResource(Res.string.settings_export_manga_desc),
            onClick = onExportMangaClick
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        SettingsNavigationItem(
            icon = TablerIcons.Outlined.Trash,
            title = stringResource(Res.string.settings_clear_cache),
            description = stringResource(Res.string.settings_clear_cache_desc, cacheSize),
            onClick = onClearCacheClick
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        SettingsNavigationItem(
            icon = TablerIcons.Outlined.InfoCircle,
            title = stringResource(Res.string.settings_about),
            description = stringResource(Res.string.settings_about_desc),
            onClick = onAboutClick
        )
    }
}

@Composable
private fun SettingsNavigationItem(
    icon: ImageVector,
    title: String,
    description: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Icon(
            imageVector = TablerIcons.Outlined.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}
