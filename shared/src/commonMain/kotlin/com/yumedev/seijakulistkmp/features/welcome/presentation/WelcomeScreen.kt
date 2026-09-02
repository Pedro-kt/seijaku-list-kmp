package com.yumedev.seijakulistkmp.features.welcome.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.features.main.presentation.MainScreen
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
                    .padding(16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                if (pagerState.currentPage < pages.size - 1) {
                    TextButton(onClick = onFinish) {
                        Text(
                            text = stringResource(Res.string.welcome_skip),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                WelcomePageContent(page = pages[page])
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (index == pagerState.currentPage) 24.dp else 8.dp)
                            .clip(if (index == pagerState.currentPage) RoundedCornerShape(4.dp) else CircleShape)
                            .background(
                                if (index == pagerState.currentPage)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
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
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun WelcomePageContent(
    page: WelcomePage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(60.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

data class WelcomePage(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
