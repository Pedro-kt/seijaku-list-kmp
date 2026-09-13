package com.yumedev.seijakulistkmp.features.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.features.auth.presentation.components.LoginTab
import com.yumedev.seijakulistkmp.features.auth.presentation.components.RegisterTab
import com.yumedev.seijakulistkmp.features.main.presentation.MainScreen
import org.jetbrains.compose.resources.stringResource
import seijakulistkmp.shared.generated.resources.*

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AuthScreenContent(
            onContinueWithoutAccount = {
                navigator.popUntilRoot()
                navigator.replace(MainScreen())
            },
            onLoginSuccess = {
                navigator.popUntilRoot()
                navigator.replace(MainScreen())
            },
            onRegisterSuccess = {
                navigator.popUntilRoot()
                navigator.replace(MainScreen())
            }
        )
    }
}

@Composable
fun AuthScreenContent(
    onContinueWithoutAccount: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        stringResource(Res.string.auth_tab_login),
        stringResource(Res.string.auth_tab_register)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = onContinueWithoutAccount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = stringResource(Res.string.auth_continue_without_account),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.auth_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (selectedTabIndex == index)
                                    FontWeight.Bold
                                else
                                    FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> LoginTab(
                    onLoginSuccess = onLoginSuccess,
                    modifier = Modifier.fillMaxSize()
                )
                1 -> RegisterTab(
                    onRegisterSuccess = onRegisterSuccess,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
