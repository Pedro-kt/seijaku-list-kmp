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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                    )
                )
        )

        // Page indicators con diseño morphing
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .width(if (index == 0) 24.dp else 8.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(
                            if (index == 0)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        )
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
        modifier = modifier.width(120.dp)
    ) {
        // Cover image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                    )
                )
        )

        // Content area - 75.dp height
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = shimmerColors,
                                start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                                end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                            )
                        )
                )

                // Metadata
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(11.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = shimmerColors,
                                start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                                end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                            )
                        )
                )

                // Genre chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .width(42.dp)
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
            // Section title
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = shimmerColors,
                            start = androidx.compose.ui.geometry.Offset(translateAnim - 1000f, 0f),
                            end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
                        )
                    )
            )

            // "See more" button
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(32.dp)
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
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        FeaturedCarouselSkeleton(modifier = Modifier.padding(top = 16.dp))

        repeat(3) {
            MediaSectionSkeleton()
        }

        Spacer(modifier = Modifier.height(80.dp))
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
