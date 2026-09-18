package com.yumedev.seijakulistkmp.features.home.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.features.home.presentation.model.AnimeCardItem
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ChevronRight

@Composable
fun AnimeSection(
    title: String,
    items: List<AnimeCardItem>,
    onSeeMoreClick: () -> Unit,
    onItemClick: (AnimeCardItem) -> Unit,
    onItemLongClick: (AnimeCardItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val responsiveValues = ResponsiveTheme.values

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = responsiveValues.horizontalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onSeeMoreClick) {
                Icon(
                    imageVector = TablerIcons.Outlined.ChevronRight,
                    contentDescription = "See more"
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(responsiveValues.spacingBetweenCards),
            contentPadding = PaddingValues(horizontal = responsiveValues.horizontalPadding)
        ) {
            items(items) { item ->
                AnimeCard(
                    item = item,
                    onClick = { onItemClick(item) },
                    onLongClick = { onItemLongClick(item) },
                    cardWidth = responsiveValues.cardWidth
                )
            }
        }
    }
}
