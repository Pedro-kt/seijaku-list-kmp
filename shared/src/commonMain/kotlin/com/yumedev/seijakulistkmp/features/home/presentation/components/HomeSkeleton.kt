package com.yumedev.seijakulistkmp.features.home.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FeaturedCarouselSkeleton(modifier: Modifier = Modifier) {
    val shimmerColors = getShimmerColors()
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                    )
                )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                )
            }
        }
    }
}

@Composable
fun MediaCardSkeleton(modifier: Modifier = Modifier) {
    val shimmerColors = getShimmerColors()
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Column(
        modifier = modifier.width(120.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                    )
                )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = shimmerColors,
                                start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                                end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun MediaSectionSkeleton(modifier: Modifier = Modifier) {
    val shimmerColors = getShimmerColors()
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = shimmerColors,
                            start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = shimmerColors,
                            start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                        )
                    )
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(5) {
                MediaCardSkeleton()
            }
        }
    }
}

@Composable
fun HomeLoadingSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        FeaturedCarouselSkeleton(modifier = Modifier.padding(top = 16.dp))

        repeat(3) {
            MediaSectionSkeleton()
        }
    }
}

@Composable
private fun getShimmerColors(): List<Color> {
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    return listOf(
        surfaceColor.copy(alpha = 0.3f),
        surfaceColor.copy(alpha = 0.5f),
        surfaceColor.copy(alpha = 0.3f)
    )
}
