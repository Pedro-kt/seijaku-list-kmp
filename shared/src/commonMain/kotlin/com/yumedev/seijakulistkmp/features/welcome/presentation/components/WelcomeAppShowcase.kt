package com.yumedev.seijakulistkmp.features.welcome.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import seijakulistkmp.shared.generated.resources.*

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WelcomeAppShowcase(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/onboarding-1-despierta.json").decodeToString()
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.fillMaxSize(0.8f),
            isPlaying = isVisible,
            restartOnPlay = true,
            iterations = 1
        )
    }
}
