package com.yumedev.seijakulistkmp.features.welcome.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.BookmarkPlus
import dev.seyfarth.tablericons.outlined.Check
import dev.seyfarth.tablericons.outlined.PlayerPause
import dev.seyfarth.tablericons.outlined.PlayerPlay
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun WelcomeOrganizationDemo(
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        CategoryData(
            name = stringResource(Res.string.list_status_watching),
            count = 5,
            icon = TablerIcons.Outlined.PlayerPlay
        ),
        CategoryData(
            name = stringResource(Res.string.list_status_completed),
            count = 15,
            icon = TablerIcons.Outlined.Check
        ),
        CategoryData(
            name = stringResource(Res.string.list_status_plan_to_watch),
            count = 8,
            icon = TablerIcons.Outlined.BookmarkPlus
        ),
        CategoryData(
            name = stringResource(Res.string.list_status_paused),
            count = 2,
            icon = TablerIcons.Outlined.PlayerPause
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        categories.forEachIndexed { index, category ->
            MinimalCategoryCard(
                category = category,
                delay = index * 100L,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MinimalCategoryCard(
    category: CategoryData,
    delay: Long,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    var animatedCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(delay)
        isVisible = true
        delay(200)
        val step = maxOf(1, category.count / 15)
        var current = 0
        while (current < category.count) {
            current = minOf(current + step, category.count)
            animatedCount = current
            delay(40)
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "category_alpha"
    )

    val translateY by animateFloatAsState(
        targetValue = if (isVisible) 0f else 20f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "category_translate"
    )

    Box(
        modifier = modifier
            .height(72.dp)
            .alpha(alpha)
            .graphicsLayer {
                translationY = translateY
            }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Text(
                    text = category.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }

            Text(
                text = animatedCount.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = (-0.5).sp
            )
        }
    }
}

private data class CategoryData(
    val name: String,
    val count: Int,
    val icon: ImageVector
)
