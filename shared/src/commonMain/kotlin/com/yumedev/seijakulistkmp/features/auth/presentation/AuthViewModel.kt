package com.yumedev.seijakulistkmp.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.seijakulistkmp.core.domain.model.Result
import com.yumedev.seijakulistkmp.features.auth.domain.model.AuthError
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.*
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.SyncProfileWithFirestoreUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.SyncMediaListWithFirestoreUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginWithEmail: LoginWithEmailUseCase,
    private val registerWithEmail: RegisterWithEmailUseCase,
    private val loginWithGoogle: LoginWithGoogleUseCase,
    private val logout: LogoutUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val isAuthenticated: IsAuthenticatedUseCase,
    private val sendPasswordReset: SendPasswordResetUseCase,
    private val syncProfileWithFirestore: SyncProfileWithFirestoreUseCase,
    private val syncMediaListWithFirestore: SyncMediaListWithFirestoreUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    private val _events = Channel<AuthEvent>()
    val events: Flow<AuthEvent> = _events.receiveAsFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            getCurrentUser().collect { user ->
                _uiState.update { it.copy(currentUser = user) }
            }
        }

        viewModelScope.launch {
            isAuthenticated().collect { authenticated ->
                _uiState.update { it.copy(isAuthenticated = authenticated) }
            }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = validatePassword(password)
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = validateConfirmPassword(_uiState.value.password, confirmPassword)
            )
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun onLogin() {
        val state = _uiState.value

        if (!state.isLoginFormValid) {
            _uiState.update {
                it.copy(
                    emailError = validateEmail(state.email),
                    passwordError = validatePassword(state.password)
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = loginWithEmail(state.email, state.password)) {
                is Result.Success -> {
                    val profileSyncResult = syncProfileWithFirestore()
                    if (profileSyncResult is Result.Failure) {
                        println("Profile sync failed: ${profileSyncResult.exception.message}")
                    }

                    val mediaSyncResult = syncMediaListWithFirestore()
                    when (mediaSyncResult) {
                        is Result.Success -> {
                            println("Media list synced: ${mediaSyncResult.data.uploaded} uploaded, ${mediaSyncResult.data.downloaded} downloaded")
                        }
                        is Result.Failure -> {
                            println("Media list sync failed: ${mediaSyncResult.exception.message}")
                        }
                    }

                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                    _events.send(AuthEvent.LoginSuccess)
                    _events.send(AuthEvent.NavigateToMain)
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception as? AuthError
                        )
                    }
                    _events.send(AuthEvent.ShowError(result.exception.message ?: "Login failed"))
                }
            }
        }
    }

    fun onRegister() {
        val state = _uiState.value

        if (!state.isRegisterFormValid) {
            _uiState.update {
                it.copy(
                    emailError = validateEmail(state.email),
                    passwordError = validatePassword(state.password),
                    confirmPasswordError = validateConfirmPassword(state.password, state.confirmPassword)
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = registerWithEmail(state.email, state.password, state.confirmPassword)) {
                is Result.Success -> {
                    val profileSyncResult = syncProfileWithFirestore()
                    if (profileSyncResult is Result.Failure) {
                        println("Profile sync failed: ${profileSyncResult.exception.message}")
                    }

                    val mediaSyncResult = syncMediaListWithFirestore()
                    when (mediaSyncResult) {
                        is Result.Success -> {
                            println("Media list synced: ${mediaSyncResult.data.uploaded} uploaded, ${mediaSyncResult.data.downloaded} downloaded")
                        }
                        is Result.Failure -> {
                            println("Media list sync failed: ${mediaSyncResult.exception.message}")
                        }
                    }

                    _uiState.update { it.copy(isLoading = false, registerSuccess = true) }
                    _events.send(AuthEvent.RegisterSuccess)
                    _events.send(AuthEvent.NavigateToMain)
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception as? AuthError
                        )
                    }
                    _events.send(AuthEvent.ShowError(result.exception.message ?: "Registration failed"))
                }
            }
        }
    }

    fun onGoogleSignIn(activityContext: Any? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGoogleSignInLoading = true, error = null) }

            when (val result = loginWithGoogle(activityContext)) {
                is Result.Success -> {
                    val profileSyncResult = syncProfileWithFirestore()
                    if (profileSyncResult is Result.Failure) {
                        println("Profile sync failed: ${profileSyncResult.exception.message}")
                    }

                    val mediaSyncResult = syncMediaListWithFirestore()
                    when (mediaSyncResult) {
                        is Result.Success -> {
                            println("Media list synced: ${mediaSyncResult.data.uploaded} uploaded, ${mediaSyncResult.data.downloaded} downloaded")
                        }
                        is Result.Failure -> {
                            println("Media list sync failed: ${mediaSyncResult.exception.message}")
                        }
                    }

                    _uiState.update { it.copy(isGoogleSignInLoading = false) }
                    _events.send(AuthEvent.GoogleSignInSuccess)
                    _events.send(AuthEvent.NavigateToMain)
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isGoogleSignInLoading = false,
                            error = result.exception as? AuthError
                        )
                    }
                    _events.send(AuthEvent.ShowError(result.exception.message ?: "Google Sign-In failed"))
                }
            }
        }
    }

    fun onForgotPassword(email: String = _uiState.value.email) {
        val emailError = validateEmail(email)
        if (emailError != null) {
            _uiState.update { it.copy(emailError = emailError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = sendPasswordReset(email)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            passwordResetEmailSent = true
                        )
                    }
                    _events.send(AuthEvent.ShowSuccess("Password reset email sent"))
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception as? AuthError
                        )
                    }
                    _events.send(AuthEvent.ShowError(result.exception.message ?: "Failed to send reset email"))
                }
            }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            logout()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearForm() {
        _uiState.update {
            AuthState()
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> null
            !email.contains("@") -> "Invalid email format"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> null
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isBlank() -> null
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }
    }
}
