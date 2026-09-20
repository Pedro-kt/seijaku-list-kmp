package com.yumedev.seijakulistkmp.ui.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object MotionTokens {
    const val DURATION_SHORT = 200
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 400
    const val DURATION_EXTRA_LONG = 500

    const val STAGGER_DELAY_SHORT = 50L
    const val STAGGER_DELAY_MEDIUM = 75L
    const val STAGGER_DELAY_LONG = 100L
}

object ExpressiveMotion {
    val quickSpring: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    val smoothSpring: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )

    val emphasizedSpring: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    val dpSpring: AnimationSpec<Dp> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    val fastEase: AnimationSpec<Float> = tween(
        durationMillis = MotionTokens.DURATION_SHORT,
        easing = EaseOut
    )

    val standardEase: AnimationSpec<Float> = tween(
        durationMillis = MotionTokens.DURATION_MEDIUM,
        easing = FastOutSlowInEasing
    )

    val slowEase: AnimationSpec<Float> = tween(
        durationMillis = MotionTokens.DURATION_LONG,

        easing = EaseInOut
    )

    val extraSlowEase: AnimationSpec<Float> = tween(
        durationMillis = MotionTokens.DURATION_EXTRA_LONG,
        easing = FastOutSlowInEasing
    )
}

object EntranceAnimations {
    const val FADE_IN_DURATION = MotionTokens.DURATION_MEDIUM
    const val SLIDE_IN_DURATION = MotionTokens.DURATION_MEDIUM
    const val SCALE_IN_DURATION = MotionTokens.DURATION_SHORT
}

object InteractionAnimations {
    const val PRESS_DURATION = MotionTokens.DURATION_SHORT
    const val RIPPLE_DURATION = MotionTokens.DURATION_MEDIUM
    const val HOVER_DURATION = MotionTokens.DURATION_SHORT

    const val PRESS_SCALE = 0.95f
    const val PRESS_ELEVATION_DP = 8f
}
