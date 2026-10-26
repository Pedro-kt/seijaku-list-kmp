package com.yumedev.seijakulistkmp.features.detail.presentation.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.core.utils.rememberImageColors
import com.yumedev.seijakulistkmp.ui.theme.SeijakuTheme
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Star
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun DetailHeader(
    coverImageUrl: String?,
    typeLabel: String?,
    demographic: String?,
    title: String,
    titleNative: String?,
    metadataText: String,
    formattedScore: String?,
    formattedVotes: String?,
    rankingText: String?,
    popularityText: String?,
    nextAiringText: String?,
    isInList: Boolean,
    onAddToListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values

    val imageColors = rememberImageColors(
        imageUrl = coverImageUrl,
        fallbackColor = MaterialTheme.colorScheme.surfaceVariant
    )

    val backgroundColor = imageColors.value.dominant

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            backgroundColor.copy(alpha = 0.5f),
            backgroundColor.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            MaterialTheme.colorScheme.surface
        ),
        startY = 0f,
        endY = 1200f
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundGradient)
            .padding(horizontal = responsive.horizontalPadding)
            .padding(top = responsive.largePadding),
        verticalArrangement = Arrangement.spacedBy(responsive.largePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(responsive.cardWidth)
                .height(responsive.cardImageHeight)
                .clip(RoundedCornerShape(responsive.cornerRadius))
        ) {
            AsyncImage(
                model = coverImageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            /*
            titleNative?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

             */
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            nextAiringText?.let { airingText ->
                NextAiringPill(airingText)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing)
        ) {
            formattedScore?.let { score ->
                StatCard(
                    modifier = Modifier.weight(1f)
                ) {
                    StatItem(
                        icon = {
                            Icon(
                                imageVector = TablerIcons.Filled.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        value = score,
                        label = formattedVotes ?: "",
                        modifier = Modifier.padding(vertical = responsive.largePadding)
                    )
                }
            }

            rankingText?.let { rank ->
                StatCard(
                    modifier = Modifier.weight(1f)
                ) {
                    StatItem(
                        value = rank,
                        label = stringResource(Res.string.detail_ranking),
                        modifier = Modifier.padding(vertical = responsive.largePadding)
                    )
                }
            }

            popularityText?.let { popularity ->
                StatCard(
                    modifier = Modifier.weight(1f)
                ) {
                    StatItem(
                        value = popularity,
                        label = stringResource(Res.string.detail_popularity),
                        modifier = Modifier.padding(vertical = responsive.largePadding)
                    )
                }
            }
        }

        if (!isInList) {
            ExpressiveAddToListButton(
                onClick = onAddToListClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale animation expresiva
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    // Elevation animation
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 6f else 2f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        onClick = { /* Stats are not clickable, just visual feedback */ },
        modifier = modifier.scale(scale),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = SeijakuTheme.shapes.cardAsymmetric,
        tonalElevation = elevation.dp,
        interactionSource = interactionSource
    ) {
        content()
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null
) {
    val responsive = ResponsiveTheme.values

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(responsive.smallSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun ExpressiveAddToListButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale animation expresiva con bounce
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    // Elevation animation
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 8f else 3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(responsive.buttonHeight)
            .scale(scale),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation.dp,
            pressedElevation = responsive.smallPadding
        ),
        interactionSource = interactionSource
    ) {
        Text(
            text = stringResource(Res.string.detail_add_to_list),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun NextAiringPill(
    airingText: String,
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values

    Surface(
        modifier = modifier,
        shape = SeijakuTheme.shapes.badge,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = airingText,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = responsive.mediumPadding, vertical = responsive.smallPadding)
        )
    }
}
