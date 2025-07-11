package com.oguzhanozgokce.bootmobilesecure.ui.login

object LoginContract {
    data class UiState(
        val isLoading: Boolean = false,
        val username: String = "",
        val password: String = "",
        val rememberMe: Boolean = false,
        val usernameError: String? = null,
        val passwordError: String? = null,
    )

    sealed interface UiAction {
        data class UsernameChanged(val username: String) : UiAction
        data class PasswordChanged(val password: String) : UiAction
        data class RememberMeChanged(val rememberMe: Boolean) : UiAction
        object LoginClicked : UiAction
        object ForgotPasswordClicked : UiAction
        object GoogleLoginClicked : UiAction
        object FacebookLoginClicked : UiAction
        object CreateAccountClicked : UiAction
    }

    sealed interface UiEffect {
        object NavigateToHome : UiEffect
        object NavigateToForgotPassword : UiEffect
        object NavigateToSignup : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}