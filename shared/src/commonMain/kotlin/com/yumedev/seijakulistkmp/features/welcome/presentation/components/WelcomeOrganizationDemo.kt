package com.yumedev.seijakulistkmp.features.welcome.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.BookmarkPlus
import dev.seyfarth.tablericons.outlined.Check
import dev.seyfarth.tablericons.outlined.PlayerPause
import dev.seyfarth.tablericons.outlined.PlayerPlay
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

// Fixed colors for each category
private val WatchingColor = Color(0xFF4CAF50)
private val WatchingBgColor = Color(0xFFE8F5E9)
private val CompletedColor = Color(0xFF2196F3)
private val CompletedBgColor = Color(0xFFE3F2FD)
private val PlanningColor = Color(0xFFFF9800)
private val PlanningBgColor = Color(0xFFFFF3E0)
private val PausedColor = Color(0xFFE91E63)
private val PausedBgColor = Color(0xFFFCE4EC)

@Composable
fun WelcomeOrganizationDemo(
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        CategoryData(
            name = stringResource(Res.string.list_status_watching),
            count = 5,
            icon = TablerIcons.Outlined.PlayerPlay,
            color = ColorType.PRIMARY,
            covers = listOf(
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx16498-C6FPmWm59CyP.jpg",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx113415-bbBWj4pEFseh.jpg"
            )
        ),
        CategoryData(
            name = stringResource(Res.string.list_status_completed),
            count = 15,
            icon = TablerIcons.Outlined.Check,
            color = ColorType.TERTIARY,
            covers = listOf(
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx101922-PEn1CTc93blC.jpg",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx21-YCDoj1EkAxFn.jpg"
            )
        ),
        CategoryData(
            name = stringResource(Res.string.list_status_plan_to_watch),
            count = 8,
            icon = TablerIcons.Outlined.BookmarkPlus,
            color = ColorType.SECONDARY,
            covers = listOf(
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx186712-d5I2TjUQcHuI.jpg",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx21459-RoPwgrZ32gM3.jpg"
            )
        ),
        CategoryData(
            name = stringResource(Res.string.list_status_paused),
            count = 2,
            icon = TablerIcons.Outlined.PlayerPause,
            color = ColorType.ERROR,
            covers = listOf(
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx20698-YZIYor2zW3Ta.png"
            )
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        repeat(2) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(2) { colIndex ->
                    val index = rowIndex * 2 + colIndex
                    if (index < categories.size) {
                        AnimatedCategoryCard(
                            category = categories[index],
                            delay = index * 120L,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedCategoryCard(
    category: CategoryData,
    delay: Long,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    var animatedCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(delay)
        isVisible = true
        delay(300)
        val step = maxOf(1, category.count / 10)
        var current = 0
        while (current < category.count) {
            current = minOf(current + step, category.count)
            animatedCount = current
            delay(50)
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "category_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "category_scale"
    )

    val (containerColor, contentColor) = when (category.color) {
        ColorType.PRIMARY -> Pair(WatchingBgColor, WatchingColor)
        ColorType.SECONDARY -> Pair(PlanningBgColor, PlanningColor)
        ColorType.TERTIARY -> Pair(CompletedBgColor, CompletedColor)
        ColorType.ERROR -> Pair(PausedBgColor, PausedColor)
    }

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .alpha(alpha)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(28.dp),
                    shape = CircleShape,
                    color = contentColor.copy(alpha = 0.15f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = contentColor
                        )
                    }
                }

                Text(
                    text = animatedCount.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (category.covers.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        category.covers.take(2).forEach { coverUrl ->
                            AsyncImage(
                                model = coverUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(24.dp)
                                    .aspectRatio(0.7f)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Text(
                    text = category.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
            }
        }
    }
}

private data class CategoryData(
    val name: String,
    val count: Int,
    val icon: ImageVector,
    val color: ColorType,
    val covers: List<String>
)

private enum class ColorType {
    PRIMARY,
    SECONDARY,
    TERTIARY,
    ERROR
}
