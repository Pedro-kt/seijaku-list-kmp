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
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import seijakulistkmp.shared.generated.resources.*

class AuthScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: AuthViewModel = koinInject()
        val state by viewModel.uiState.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is AuthEvent.NavigateToMain -> {
                        navigator.popUntilRoot()
                        navigator.replace(MainScreen())
                    }
                    is AuthEvent.ShowError -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                    is AuthEvent.ShowSuccess -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                    else -> { /* Handle other events if needed */ }
                }
            }
        }

        AuthScreenContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
            onToggleConfirmPasswordVisibility = viewModel::onToggleConfirmPasswordVisibility,
            onLogin = viewModel::onLogin,
            onRegister = viewModel::onRegister,
            onGoogleSignIn = viewModel::onGoogleSignIn,
            onForgotPassword = viewModel::onForgotPassword,
            onContinueWithoutAccount = {
                navigator.popUntilRoot()
                navigator.replace(MainScreen())
            }
        )
    }
}

@Composable
fun AuthScreenContent(
    state: AuthState,
    snackbarHostState: SnackbarHostState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onGoogleSignIn: (Any?) -> Unit,
    onForgotPassword: (String) -> Unit,
    onContinueWithoutAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        stringResource(Res.string.auth_tab_login),
        stringResource(Res.string.auth_tab_register)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    state = state,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onTogglePasswordVisibility = onTogglePasswordVisibility,
                    onLogin = onLogin,
                    onGoogleSignIn = onGoogleSignIn,
                    onForgotPassword = onForgotPassword,
                    modifier = Modifier.fillMaxSize()
                )
                1 -> RegisterTab(
                    state = state,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onTogglePasswordVisibility = onTogglePasswordVisibility,
                    onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
                    onRegister = onRegister,
                    onGoogleSignIn = onGoogleSignIn,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
