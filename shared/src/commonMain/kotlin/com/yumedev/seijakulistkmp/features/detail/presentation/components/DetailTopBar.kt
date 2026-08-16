package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowLeft
import dev.seyfarth.tablericons.outlined.Share
import dev.seyfarth.tablericons.outlined.Star

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    isFavorite: Boolean = false,
    title: String? = null,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.ArrowLeft,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.Star,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.Share,
                    contentDescription = "Share"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        modifier = modifier
    )
}
