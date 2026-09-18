package com.yumedev.seijakulistkmp.features.home.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.core.utils.rememberImageColors
import com.yumedev.seijakulistkmp.features.home.presentation.model.FeaturedMediaItem
import com.yumedev.seijakulistkmp.ui.theme.ExpressiveMotion
import com.yumedev.seijakulistkmp.ui.theme.InteractionAnimations
import com.yumedev.seijakulistkmp.ui.theme.SeijakuTheme
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Star
import dev.seyfarth.tablericons.outlined.ChevronRight
import dev.seyfarth.tablericons.outlined.PlayerPlay
import dev.seyfarth.tablericons.outlined.Check
import dev.seyfarth.tablericons.outlined.Clock
import dev.seyfarth.tablericons.outlined.X
import dev.seyfarth.tablericons.outlined.PlayerPause
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.detail
import seijakulistkmp.shared.generated.resources.status_airing
import seijakulistkmp.shared.generated.resources.status_publishing
import seijakulistkmp.shared.generated.resources.status_finished
import seijakulistkmp.shared.generated.resources.status_not_yet_released
import seijakulistkmp.shared.generated.resources.status_not_yet_aired
import seijakulistkmp.shared.generated.resources.status_cancelled
import seijakulistkmp.shared.generated.resources.status_hiatus

@Composable
fun FeaturedCarousel(
    items: List<FeaturedMediaItem>,
    currentPage: Int,
    modifier: Modifier = Modifier,
    onItemClick: (FeaturedMediaItem) -> Unit = {},
    onUserInteraction: () -> Unit = {}
) {
    if (items.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { items.size }
    )

    LaunchedEffect(currentPage) {
        if (pagerState.currentPage != currentPage) {
            pagerState.animateScrollToPage(currentPage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect {
                onUserInteraction()
            }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Carousel
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            FeaturedCarouselItem(
                item = items[page],
                onClick = {
                    onUserInteraction()
                    onItemClick(items[page])
                }
            )
        }

        // Expressive Page Indicators
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(items.size) { index ->
                ExpressivePageIndicator(
                    isActive = pagerState.currentPage == index
                )
            }
        }
    }
}

@Composable
private fun FeaturedCarouselItem(
    item: FeaturedMediaItem,
    onClick: () -> Unit
) {
    val imageUrl = item.coverImageUrl

    val imageColors = rememberImageColors(
        imageUrl = imageUrl,
        fallbackColor = MaterialTheme.colorScheme.surfaceContainer
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = ExpressiveMotion.quickSpring
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .scale(scale),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C1C1E)
        ),
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            imageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                imageColors.value.dominant
                            ),
                            startY = 0f,
                            endY = 440f
                        )
                    )
            )

            item.status?.let { rawStatus ->
                val statusText = when (rawStatus) {
                    "RELEASING" -> if (item.isManga) {
                        stringResource(Res.string.status_publishing)
                    } else {
                        stringResource(Res.string.status_airing)
                    }
                    "FINISHED" -> stringResource(Res.string.status_finished)
                    "NOT_YET_RELEASED" -> if (item.isManga) {
                        stringResource(Res.string.status_not_yet_released)
                    } else {
                        stringResource(Res.string.status_not_yet_aired)
                    }
                    "CANCELLED" -> stringResource(Res.string.status_cancelled)
                    "HIATUS" -> stringResource(Res.string.status_hiatus)
                    else -> rawStatus
                }

                val statusIcon = when (rawStatus) {
                    "RELEASING" -> TablerIcons.Outlined.PlayerPlay
                    "FINISHED" -> TablerIcons.Outlined.Check
                    "NOT_YET_RELEASED" -> TablerIcons.Outlined.Clock
                    "CANCELLED" -> TablerIcons.Outlined.X
                    "HIATUS" -> TablerIcons.Outlined.PlayerPause
                    else -> TablerIcons.Outlined.PlayerPlay
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    shape = SeijakuTheme.shapes.chip,
                    color = imageColors.value.vibrant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = imageColors.value.vibrant.getContrastColor()
                        )
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall,
                            color = imageColors.value.vibrant.getContrastColor(),
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

                item.rating?.let { rating ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = SeijakuTheme.shapes.chip,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = TablerIcons.Filled.Star,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = rating,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = imageColors.value.dominant.getContrastColor(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.metadata.isNotEmpty()) {
                    Text(
                        text = item.metadata,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = imageColors.value.dominant.getContrastColor().copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun Color.getContrastColor(): Color {
    val luminance = (0.299 * red + 0.587 * green + 0.114 * blue)
    return if (luminance > 0.5f) Color.Black else Color.White
}

@Composable
private fun ExpressivePageIndicator(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val width by animateDpAsState(
        targetValue = if (isActive) 24.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val height by animateDpAsState(
        targetValue = 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val scale by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )

    val alpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .scale(scale)
            .clip(RoundedCornerShape(50))
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = alpha)
            )
    )
}
