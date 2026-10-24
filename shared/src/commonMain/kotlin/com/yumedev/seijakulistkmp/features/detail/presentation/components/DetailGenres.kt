package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.ui.theme.SeijakuTheme
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun DetailGenres(
    genres: List<String>,
    modifier: Modifier = Modifier
) {
    if (genres.isEmpty()) return

    val responsive = ResponsiveTheme.values
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = responsive.horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(responsive.mediumPadding)
    ) {
        Text(
            text = stringResource(Res.string.detail_genres),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
            verticalArrangement = Arrangement.spacedBy(responsive.mediumSpacing)
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
    val responsive = ResponsiveTheme.values
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        onClick = { },
        shape = SeijakuTheme.shapes.chip,
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = elevation.dp,
        modifier = modifier.scale(scale),
        interactionSource = interactionSource
    ) {
        Text(
            text = genre,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = responsive.horizontalPadding, vertical = responsive.mediumSpacing)
        )
    }
}
