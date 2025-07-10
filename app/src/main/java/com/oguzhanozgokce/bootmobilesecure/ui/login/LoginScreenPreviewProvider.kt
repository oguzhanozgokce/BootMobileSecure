package com.oguzhanozgokce.bootmobilesecure.ui.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class LoginScreenPreviewProvider : PreviewParameterProvider<LoginContract.UiState> {
    override val values: Sequence<LoginContract.UiState>
        get() = sequenceOf(
            LoginContract.UiState(
                isLoading = false,
                username = "",
                password = "",
                rememberMe = false,
                usernameError = null,
                passwordError = null,
            ),
            LoginContract.UiState(
                isLoading = false,
                username = "user@example.com",
                password = "password123",
                rememberMe = true,
                usernameError = null,
                passwordError = null,
            ),
            LoginContract.UiState(
                isLoading = false,
                username = "invalid-email",
                password = "123",
                rememberMe = false,
                usernameError = "Please enter a valid email address",
                passwordError = "Password must be at least 8 characters",
            ),
            LoginContract.UiState(
                isLoading = true,
                username = "user@example.com",
                password = "password123",
                rememberMe = true,
                usernameError = null,
                passwordError = null,
            ),
        )
}