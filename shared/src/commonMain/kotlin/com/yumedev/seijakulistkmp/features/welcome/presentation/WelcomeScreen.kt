package com.yumedev.seijakulistkmp.features.welcome.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.features.main.presentation.MainScreen
import com.yumedev.seijakulistkmp.features.welcome.presentation.components.WelcomeExportDemo
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Book
import dev.seyfarth.tablericons.outlined.DeviceTv
import dev.seyfarth.tablericons.outlined.FileDownload
import dev.seyfarth.tablericons.outlined.Refresh
import dev.seyfarth.tablericons.outlined.Star
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

class WelcomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        WelcomeScreenContent(
            onFinish = {
                navigator.popUntilRoot()
                navigator.replace(MainScreen())
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreenContent(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        WelcomePage(
            title = stringResource(Res.string.welcome_page1_title),
            description = stringResource(Res.string.welcome_page1_description),
            icon = TablerIcons.Outlined.DeviceTv
        ),
        WelcomePage(
            title = stringResource(Res.string.welcome_page2_title),
            description = stringResource(Res.string.welcome_page2_description),
            icon = TablerIcons.Outlined.Star
        ),
        WelcomePage(
            title = stringResource(Res.string.welcome_page3_title),
            description = stringResource(Res.string.welcome_page3_description),
            icon = TablerIcons.Outlined.FileDownload
        ),
        WelcomePage(
            title = stringResource(Res.string.welcome_page4_title),
            description = stringResource(Res.string.welcome_page4_description),
            icon = TablerIcons.Outlined.Book
        ),
        WelcomePage(
            title = stringResource(Res.string.welcome_page5_title),
            description = stringResource(Res.string.welcome_page5_description),
            icon = TablerIcons.Outlined.Refresh
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                val skipButtonAlpha by animateFloatAsState(
                    targetValue = if (pagerState.currentPage < pages.size - 1) 1f else 0f,
                    animationSpec = tween(durationMillis = 300),
                    label = "skip_button_alpha"
                )

                TextButton(
                    onClick = onFinish,
                    enabled = pagerState.currentPage < pages.size - 1,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.alpha(skipButtonAlpha)
                ) {
                    Text(
                        text = stringResource(Res.string.welcome_skip),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val pageOffset = (
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                ).absoluteValue

                WelcomePageContent(
                    page = pages[page],
                    pageIndex = page,
                    pageOffset = pageOffset
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { index ->
                    PageIndicator(
                        isSelected = index == pagerState.currentPage,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                FilledTonalButton(
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = if (pagerState.currentPage < pages.size - 1)
                            stringResource(Res.string.welcome_next)
                        else
                            stringResource(Res.string.welcome_get_started),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomePageContent(
    page: WelcomePage,
    pageIndex: Int,
    pageOffset: Float,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val isCompact = screenWidthDp < 360
    val isSmall = screenWidthDp < 400

    val titleFontSize = when {
        isCompact -> 28.sp
        isSmall -> 36.sp
        screenWidthDp < 600 -> 45.sp
        else -> 57.sp
    }

    val descriptionFontSize = when {
        isCompact -> 13.sp
        isSmall -> 14.sp
        else -> 16.sp
    }

    val descriptionLineHeight = when {
        isCompact -> 20.sp
        isSmall -> 22.sp
        else -> 28.sp
    }

    val titleLetterSpacing = when {
        isCompact -> 0.5.sp
        isSmall -> 1.sp
        else -> 2.sp
    }

    val horizontalPadding = when {
        isCompact -> 20.dp
        isSmall -> 24.dp
        else -> 32.dp
    }

    val verticalPadding = when {
        isCompact -> 16.dp
        isSmall -> 20.dp
        else -> 24.dp
    }

    val contentSpacing = when {
        isCompact -> 8.dp
        isSmall -> 12.dp
        else -> 16.dp
    }

    val visualWeight = when {
        isCompact -> 0.45f
        isSmall -> 0.50f
        else -> 0.55f
    }

    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        contentVisible = true
    }

    val titleAlpha by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 200),
        label = "title_alpha"
    )

    val descAlpha by animateFloatAsState(
        targetValue = if (contentVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, delayMillis = 350),
        label = "desc_alpha"
    )

    val titleTranslateY = lerp(20f, 0f, titleAlpha)
    val descTranslateY = lerp(20f, 0f, descAlpha)

    val visualOffset = lerp(0f, -30f, pageOffset.coerceIn(0f, 1f))
    val contentOffset = lerp(0f, 30f, pageOffset.coerceIn(0f, 1f))
    val visualAlpha = lerp(1f, 0.3f, pageOffset.coerceIn(0f, 1f))
    val contentAlpha = lerp(1f, 0.5f, pageOffset.coerceIn(0f, 1f))

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(visualWeight)
                .graphicsLayer {
                    translationY = visualOffset
                    alpha = visualAlpha
                },
            contentAlignment = Alignment.Center
        ) {
            WelcomeVisualShowcase(
                pageIndex = pageIndex,
                page = page,
                pageOffset = pageOffset
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f - visualWeight)
                .graphicsLayer {
                    translationY = contentOffset
                    alpha = contentAlpha
                }
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalArrangement = Arrangement.spacedBy(contentSpacing)
        ) {
            Text(
                text = page.title.uppercase(),
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = titleLetterSpacing,
                lineHeight = (titleFontSize.value * 1.15).sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(titleAlpha)
                    .graphicsLayer {
                        translationY = titleTranslateY
                    }
            )

            Text(
                text = page.description,
                fontSize = descriptionFontSize,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = descriptionLineHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(descAlpha)
                    .graphicsLayer {
                        translationY = descTranslateY
                    }
            )
        }
    }
}

@Composable
private fun WelcomeVisualShowcase(
    pageIndex: Int,
    page: WelcomePage,
    pageOffset: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (pageIndex) {
            0 -> {

            }
            1 -> {

            }
            2 -> {
                WelcomeExportDemo(modifier = Modifier.fillMaxSize())
            }
            3 -> {

            }
            else -> {
                // Clean space for future custom designs
            }
        }
    }
}

@Composable
private fun PageIndicator(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 32.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 300f
        ),
        label = "indicator_width"
    )

    val color by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
        animationSpec = tween(durationMillis = 300),
        label = "indicator_color"
    )

    Box(
        modifier = modifier
            .width(width)
            .height(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}

data class WelcomePage(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
