package com.yumedev.seijakulistkmp.features.welcome.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun WelcomeAppShowcase(
    modifier: Modifier = Modifier
) {
    val animeCovers = remember {
        listOf(
            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx16498-C6FPmWm59CyP.jpg",
            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx5680-r3AI3Cwfv0Aq.png",
            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx113415-bbBWj4pEFseh.jpg",
            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx186712-d5I2TjUQcHuI.jpg",
            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx20698-YZIYor2zW3Ta.png"
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            repeat(3) { index ->
                AnimatedAnimeCard(
                    coverUrl = animeCovers[index],
                    delay = index * 100L,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(0.65f),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            repeat(2) { index ->
                AnimatedAnimeCard(
                    coverUrl = animeCovers[3 + index],
                    delay = (3 + index) * 100L,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun AnimatedAnimeCard(
    coverUrl: String,
    delay: Long,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "card_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    val offsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else 20f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_offset"
    )

    AsyncImage(
        model = coverUrl,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(10.dp))
            .alpha(alpha)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = offsetY
            },
        contentScale = ContentScale.Crop
    )
}
