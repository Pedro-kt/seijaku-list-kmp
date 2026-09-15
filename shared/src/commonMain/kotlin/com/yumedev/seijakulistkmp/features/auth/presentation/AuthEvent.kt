package com.yumedev.seijakulistkmp.features.auth.presentation

sealed class AuthEvent {
    data object NavigateToMain : AuthEvent()
    data object LoginSuccess : AuthEvent()
    data object RegisterSuccess : AuthEvent()
    data object GoogleSignInSuccess : AuthEvent()
    data class ShowError(val message: String) : AuthEvent()
    data class ShowSuccess(val message: String) : AuthEvent()
}
