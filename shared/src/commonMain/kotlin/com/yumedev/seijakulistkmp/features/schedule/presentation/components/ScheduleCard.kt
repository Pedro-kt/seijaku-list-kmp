package com.yumedev.seijakulistkmp.features.schedule.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.seijakulistkmp.core.util.AiringScheduleTimeUtil
import com.yumedev.seijakulistkmp.features.schedule.domain.model.AiringScheduleItem
import com.yumedev.seijakulistkmp.ui.theme.ExpressiveMotion
import com.yumedev.seijakulistkmp.ui.theme.InteractionAnimations
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Circle
import dev.seyfarth.tablericons.filled.Star
import dev.seyfarth.tablericons.outlined.Clock
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.Res
import seijakulistkmp.shared.generated.resources.*
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme

@Composable
fun ScheduleCard(
    item: AiringScheduleItem,
    timezone: TimeZone,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showTime: Boolean = false,
    showDivider: Boolean = true
) {
    val responsive = ResponsiveTheme.values
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) InteractionAnimations.PRESS_SCALE else 1f,
        animationSpec = ExpressiveMotion.quickSpring
    )

    var currentTimeUntilAiring by remember { mutableStateOf(item.timeUntilAiring) }

    LaunchedEffect(item.id) {
        while (true) {
            currentTimeUntilAiring = AiringScheduleTimeUtil.calculateTimeUntilAiring(item.airingAt)
            delay(60_000)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onClick(item.mediaId)
                }
                .padding(vertical = responsive.mediumPadding),
            horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing)
        ) {
            item.coverImageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = item.mediaTitle,
                    modifier = Modifier
                        .width(100.dp)
                        .height(140.dp)
                        .clip(RoundedCornerShape(responsive.smallSpacing)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(responsive.smallSpacing)
            ) {
                Text(
                    text = item.mediaTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(responsive.mediumSpacing),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val episodeText = if (item.totalEpisodes != null) {
                        "${stringResource(Res.string.schedule_episode)} ${item.episode}/${item.totalEpisodes}"
                    } else {
                        "${stringResource(Res.string.schedule_episode)} ${item.episode}"
                    }

                    Text(
                        text = episodeText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    item.averageScore?.let { score ->
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(responsive.smallPadding),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = TablerIcons.Filled.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${score / 10.0}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (showTime || currentTimeUntilAiring > 0) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(responsive.smallPadding)
                    ) {
                        if (showTime) {
                            Text(
                                text = AiringScheduleTimeUtil.formatAiringTime(
                                    timestamp = item.airingAt,
                                    timeZone = timezone,
                                    use24Hour = false
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (currentTimeUntilAiring > 0) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(responsive.smallSpacing),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = TablerIcons.Outlined.Clock,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(responsive.mediumSpacing)
                                )
                                Text(
                                    text = "${stringResource(Res.string.schedule_in)} ${AiringScheduleTimeUtil.formatCountdown(currentTimeUntilAiring)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 116.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        }
    }
}
