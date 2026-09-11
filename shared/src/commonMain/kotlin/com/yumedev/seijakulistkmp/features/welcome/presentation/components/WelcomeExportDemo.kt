package com.yumedev.seijakulistkmp.features.welcome.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowRight
import dev.seyfarth.tablericons.outlined.Check
import dev.seyfarth.tablericons.outlined.FileExport
import dev.seyfarth.tablericons.outlined.FileTypeXml
import dev.seyfarth.tablericons.outlined.ListDetails
import kotlinx.coroutines.delay

@Composable
fun WelcomeExportDemo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Export flow animation
        ExportFlowAnimation()

        Spacer(modifier = Modifier.height(8.dp))

        // Supported platforms cards
        SupportedPlatformsCards()
    }
}

@Composable
private fun ExportFlowAnimation() {
    var currentStep by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            currentStep = (currentStep + 1) % 3
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Step 1: Your List
        FlowStep(
            icon = TablerIcons.Outlined.ListDetails,
            label = "Your List",
            isActive = currentStep >= 0,
            delay = 0
        )

        // Arrow 1
        AnimatedArrow(isActive = currentStep >= 1)

        // Step 2: Export
        FlowStep(
            icon = TablerIcons.Outlined.FileExport,
            label = "Export",
            isActive = currentStep >= 1,
            delay = 200
        )

        // Arrow 2
        AnimatedArrow(isActive = currentStep >= 2)

        // Step 3: XML File
        FlowStep(
            icon = TablerIcons.Outlined.FileTypeXml,
            label = "XML",
            isActive = currentStep >= 2,
            delay = 400
        )
    }
}

@Composable
private fun FlowStep(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    delay: Long
) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(isActive) {
        if (isActive) {
            delay(delay)
            showContent = true
        } else {
            showContent = false
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (showContent) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "step_scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (showContent) 1f else 0.4f,
        animationSpec = tween(durationMillis = 300),
        label = "step_alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .alpha(alpha)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = if (showContent)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = if (showContent) 4.dp else 0.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(28.dp),
                    tint = if (showContent)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (showContent) FontWeight.SemiBold else FontWeight.Normal,
            color = if (showContent)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AnimatedArrow(isActive: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300),
        label = "arrow_alpha"
    )

    val offsetX by animateFloatAsState(
        targetValue = if (isActive) 4f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "arrow_offset"
    )

    Icon(
        imageVector = TablerIcons.Outlined.ArrowRight,
        contentDescription = null,
        modifier = Modifier
            .size(20.dp)
            .alpha(alpha)
            .graphicsLayer {
                translationX = offsetX
            },
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun SupportedPlatformsCards() {
    val platforms = listOf(
        PlatformInfo("MAL", "MyAnimeList", MaterialTheme.colorScheme.primaryContainer),
        PlatformInfo("AniList", "AniList.co", MaterialTheme.colorScheme.secondaryContainer)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        platforms.forEachIndexed { index, platform ->
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(600 + (index * 150L))
                isVisible = true
            }

            val alpha by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(durationMillis = 400),
                label = "platform_alpha_$index"
            )

            val offsetY by animateFloatAsState(
                targetValue = if (isVisible) 0f else 20f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "platform_offset_$index"
            )

            Card(
                modifier = Modifier
                    .weight(1f)
                    .alpha(alpha)
                    .offset(y = offsetY.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = platform.color
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Check icon
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiary
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = TablerIcons.Outlined.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onTertiary
                            )
                        }
                    }

                    Text(
                        text = platform.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = platform.fullName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private data class PlatformInfo(
    val name: String,
    val fullName: String,
    val color: androidx.compose.ui.graphics.Color
)
