package com.yumedev.seijakulistkmp.features.welcome.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.CloudOff
import dev.seyfarth.tablericons.outlined.Database
import dev.seyfarth.tablericons.outlined.DeviceFloppy
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

@Composable
fun WelcomeOfflineDemo(
    modifier: Modifier = Modifier
) {
    val features = listOf(
        OfflineFeature(
            icon = TablerIcons.Outlined.Database,
            title = stringResource(Res.string.welcome_offline_feature1_title),
            description = stringResource(Res.string.welcome_offline_feature1_description)
        ),
        OfflineFeature(
            icon = TablerIcons.Outlined.CloudOff,
            title = stringResource(Res.string.welcome_offline_feature2_title),
            description = stringResource(Res.string.welcome_offline_feature2_description)
        ),
        OfflineFeature(
            icon = TablerIcons.Outlined.DeviceFloppy,
            title = stringResource(Res.string.welcome_offline_feature3_title),
            description = stringResource(Res.string.welcome_offline_feature3_description)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        features.forEachIndexed { index, feature ->
            AnimatedFeatureItem(
                feature = feature,
                delay = index * 200L
            )
        }
    }
}

@Composable
private fun AnimatedFeatureItem(
    feature: OfflineFeature,
    delay: Long
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "feature_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "feature_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 2.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Text(
            text = feature.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = feature.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

private data class OfflineFeature(
    val icon: ImageVector,
    val title: String,
    val description: String
)
