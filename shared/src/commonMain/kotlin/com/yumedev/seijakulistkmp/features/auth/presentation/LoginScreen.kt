package com.yumedev.seijakulistkmp.features.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.yumedev.seijakulistkmp.core.utils.getPlatformContext
import com.yumedev.seijakulistkmp.features.auth.presentation.components.ForgotPasswordDialog
import com.yumedev.seijakulistkmp.features.auth.presentation.components.SocialAuthButtons
import com.yumedev.seijakulistkmp.features.main.presentation.MainScreen
import com.yumedev.seijakulistkmp.ui.theme.ResponsiveTheme
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Eye
import dev.seyfarth.tablericons.outlined.EyeOff
import dev.seyfarth.tablericons.outlined.Lock
import dev.seyfarth.tablericons.outlined.Mail
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import seijakulistkmp.shared.generated.resources.*

class LoginScreen : Screen {
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
                    else -> {}
                }
            }
        }

        LoginScreenContent(
            state = state,
            snackbarHostState = snackbarHostState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
            onLogin = viewModel::onLogin,
            onGoogleSignIn = viewModel::onGoogleSignIn,
            onForgotPassword = viewModel::onForgotPassword,
            onNavigateToRegister = {
                navigator.push(RegisterScreen())
            },
            onContinueWithoutAccount = {
                navigator.popUntilRoot()
                navigator.replace(MainScreen())
            }
        )
    }
}

@Composable
fun LoginScreenContent(
    state: AuthState,
    snackbarHostState: SnackbarHostState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLogin: () -> Unit,
    onGoogleSignIn: (Any?) -> Unit,
    onForgotPassword: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onContinueWithoutAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val responsiveValues = ResponsiveTheme.values
    val focusManager = LocalFocusManager.current
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(responsiveValues.horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = onContinueWithoutAccount,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(responsiveValues.buttonHeight),
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
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(responsiveValues.horizontalPadding),
            verticalArrangement = Arrangement.spacedBy(responsiveValues.verticalPadding)
        ) {
            Spacer(modifier = Modifier.height(responsiveValues.largePadding))

            Text(
                text = stringResource(Res.string.auth_login_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(Res.string.auth_login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(Res.string.auth_email)) },
                placeholder = { Text(stringResource(Res.string.auth_email_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.emailError != null,
                supportingText = state.emailError?.let { { Text(it) } },
                leadingIcon = {
                    Icon(
                        imageVector = TablerIcons.Outlined.Mail,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(Res.string.auth_password)) },
                placeholder = { Text(stringResource(Res.string.auth_password_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.passwordError != null,
                supportingText = state.passwordError?.let { { Text(it) } },
                leadingIcon = {
                    Icon(
                        imageVector = TablerIcons.Outlined.Lock,
                        contentDescription = null
                    )
                },
                visualTransformation = if (state.passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (state.isLoginFormValid) onLogin()
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (state.passwordVisible)
                                TablerIcons.Outlined.EyeOff
                            else
                                TablerIcons.Outlined.Eye,
                            contentDescription = if (state.passwordVisible)
                                stringResource(Res.string.auth_hide_password)
                            else
                                stringResource(Res.string.auth_show_password)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading
            )

            TextButton(
                onClick = { showForgotPasswordDialog = true },
                modifier = Modifier.align(Alignment.End),
                enabled = !state.isLoading
            ) {
                Text(
                    text = stringResource(Res.string.auth_forgot_password),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(responsiveValues.buttonHeight),
                shape = RoundedCornerShape(12.dp),
                enabled = state.isLoginFormValid && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.auth_login_button),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(Res.string.auth_or_continue_with),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            val platformContext = getPlatformContext()

            SocialAuthButtons(
                isGoogleLoading = state.isGoogleSignInLoading,
                onGoogleSignIn = { onGoogleSignIn(platformContext) },
                buttonText = stringResource(Res.string.auth_google_continue),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.auth_dont_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = onNavigateToRegister,
                    enabled = !state.isLoading
                ) {
                    Text(
                        text = stringResource(Res.string.auth_register_link),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            initialEmail = state.email,
            isLoading = state.isLoading,
            onDismiss = { showForgotPasswordDialog = false },
            onSendResetEmail = { email ->
                onForgotPassword(email)
                showForgotPasswordDialog = false
            }
        )
    }
}
