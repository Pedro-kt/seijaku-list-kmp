package com.yumedev.seijakulistkmp.features.schedule.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun ScheduleCardSkeleton(
    modifier: Modifier = Modifier
) {
    val responsive = ResponsiveTheme.values
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 1000f, translateAnim - 1000f),
        end = Offset(translateAnim, translateAnim)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(responsive.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = responsive.smallPadding
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(responsive.smallPadding)
        ) {
            Box(
                modifier = Modifier
                    .width(responsive.cardWidth / 2)
                    .height(responsive.minTouchTarget * 2)
                    .clip(
                        RoundedCornerShape(
                            topStart = responsive.cornerRadius,
                            bottomStart = responsive.cornerRadius,
                            topEnd = responsive.smallPadding,
                            bottomEnd = responsive.smallPadding
                        )
                    )
                    .background(brush)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(responsive.minTouchTarget * 2)
                    .padding(responsive.mediumSpacing),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(responsive.smallSpacing)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(responsive.smallPadding))
                            .background(brush)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(responsive.smallPadding))
                            .background(brush)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(12.dp)
                                .clip(RoundedCornerShape(responsive.smallPadding))
                                .background(brush)
                        )

                        Box(
                            modifier = Modifier
                                .size(responsive.mediumSpacing)
                                .clip(CircleShape)
                                .background(brush)
                        )

                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(12.dp)
                                .clip(RoundedCornerShape(responsive.smallPadding))
                                .background(brush)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(responsive.smallPadding))
                        .background(brush)
                )
            }
        }
    }
}
