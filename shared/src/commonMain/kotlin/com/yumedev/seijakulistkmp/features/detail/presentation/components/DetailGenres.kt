package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun DetailGenres(
    genres: List<String>,
    modifier: Modifier = Modifier
) {
    if (genres.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.detail_genres),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                GenreChip(genre = genre)
            }
        }
    }
}

@Composable
private fun GenreChip(
    genre: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
    ) {
        Text(
            text = genre,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
